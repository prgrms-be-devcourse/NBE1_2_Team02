package com.example.book_your_seat.payment.service.facade;

import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.service.ConcertQueryService;
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.facade.CouponCommandService;
import com.example.book_your_seat.coupon.facade.CouponQueryService;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.domain.PaymentStatus;
import com.example.book_your_seat.payment.service.PaymentCommandService;
import com.example.book_your_seat.payment.service.dto.PaymentCommand;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.reservation.service.ReservationCommandService;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.query.AddressQueryService;
import com.example.book_your_seat.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PaymentFacadeImpl implements PaymentFacade {

    private final AddressQueryService addressQueryService;

    private final PaymentCommandService paymentCommandService;
    private final ReservationCommandService reservationCommandService;

    private final CouponQueryService couponQueryService;
    private final CouponCommandService couponCommandService;

    private final ConcertQueryService concertQueryService;


    @Override
    public ConfirmResponse processPayment(final PaymentCommand command) {

        Payment payment = createPayment(command);
        Reservation reservation = createReservation(command, payment);

        paymentCommandService.savePayment(payment);
        Reservation savedReservation = reservationCommandService.saveReservation(reservation);

        couponCommandService.useUserCoupon(command.userCouponId);
        ConcertResponse concert = concertQueryService.findById(command.concertId);

        return ConfirmResponse.builder()
                .userId(command.userId)
                .reservationId(savedReservation.getId())
                .concludePrice(command.totalAmount)
                .status(savedReservation.getStatus())
                .concertTitle(concert.getTitle())
                .concertStartHour(concert.getStartHour())
                .seatsId(command.seatIds)
                .build();
    }

    private Reservation createReservation(PaymentCommand command, Payment payment) {
        Address address = addressQueryService.getAddressWithUser(command.addressId);
        User user = address.getUser();

        return Reservation.builder()
                .user(user)
                .address(address)
                .payment(payment)
                .status(ReservationStatus.ORDERED)
                .build();
    }

    private Payment createPayment(PaymentCommand command) {
        CouponDetailResponse couponResponse = couponQueryService.getCouponDetailById(command.userCouponId);

        return Payment.builder()
                .totalPrice(command.totalAmount)
                .paymentStatus(PaymentStatus.COMPLETED)
                .expiryAt(command.approvedAt)
                .discountRate(couponResponse.discountRate())
                .build();
    }
}
