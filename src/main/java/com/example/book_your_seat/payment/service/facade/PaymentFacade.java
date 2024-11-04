package com.example.book_your_seat.payment.service.facade;

import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.service.query.ConcertQueryService;
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.service.command.CouponCommandService;
import com.example.book_your_seat.coupon.service.query.CouponQueryService;

import com.example.book_your_seat.payment.controller.dto.request.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.controller.dto.response.FinalPriceResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.domain.PaymentStatus;
import com.example.book_your_seat.payment.service.command.PaymentCommandService;
import com.example.book_your_seat.payment.service.dto.PaymentCommand;
import com.example.book_your_seat.queue.service.command.QueueCommandService;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.reservation.service.command.ReservationCommandService;
import com.example.book_your_seat.seat.domain.SeatId;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.query.AddressQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class PaymentFacade {

    private final AddressQueryService addressQueryService;

    private final PaymentCommandService paymentCommandService;
    private final ReservationCommandService reservationCommandService;

    private final CouponQueryService couponQueryService;
    private final CouponCommandService couponCommandService;

    private final ConcertQueryService concertQueryService;
    private final SeatQueryService seatQueryService;

    private final QueueCommandService queueCommandService;

    public ConfirmResponse processPayment(final PaymentCommand command, Long userId, String token) {

        Payment payment = createPayment(command);
        Reservation reservation = createReservation(command, payment);

        paymentCommandService.savePayment(payment);
        reservationCommandService.saveReservation(reservation);

        couponCommandService.useUserCoupon(command.userCouponId);
        ConcertResponse concert = concertQueryService.findById(command.concertId);


        List<Integer> seatNumbers = seatQueryService.findSeatNumbers(command.seatIds);
        queueCommandService.removeTokenInProcessingQueue(userId, command.concertId, token);

        return ConfirmResponse.builder()
                .userId(userId)
                .reservationId(reservation.getId())
                .concludePrice(command.totalAmount)
                .status(reservation.getStatus())
                .concertTitle(concert.getTitle())
                .concertStartHour(concert.getStartHour())
                .seatNumbers(
                        command.seatIds.stream()
                        .map(SeatId::getSeatNumber)
                        .toList()
                )
                .build();
    }

    private Reservation createReservation(PaymentCommand command, Payment payment) {
        Address address = addressQueryService.getAddressWithUser(command.addressId);
        User user = address.getUser();

        return Reservation.builder()
                .userId(user.getId())
                .address(address)
                .paymentId(payment.getId())
                .seatIds(command.seatIds)
                .build();
    }

    private Payment createPayment(PaymentCommand command) {
        CouponDetailResponse couponResponse = couponQueryService.getCouponDetailById(command.userCouponId);

        return Payment.builder()
                .totalPrice(command.totalAmount)
                .paymentStatus(PaymentStatus.COMPLETED)
                .expiryAt(command.approvedAt)
                .discountRate(couponResponse.discountRate())
                .paymentKey(command.paymentKey)
                .build();
    }

    @Transactional(readOnly = true)
    public FinalPriceResponse getFinalPrice(final FinalPriceRequest request) {

        Integer originPrice = seatQueryService.getSeatPrice(request);
        DiscountRate discountRate = couponQueryService.getDiscountRate(request.userCouponId());

        return new FinalPriceResponse(calculateDiscountPrice(originPrice, discountRate));
    }

    private BigDecimal calculateDiscountPrice(Integer seatPrice, DiscountRate discountRate) {
        return BigDecimal.valueOf(seatPrice)
                .multiply(BigDecimal.ONE.subtract(
                        BigDecimal.valueOf(discountRate.getValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                ))
                .setScale(0, RoundingMode.DOWN);
    }
}
