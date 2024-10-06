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
import com.example.book_your_seat.user.service.AddressQueryService;
import com.example.book_your_seat.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserQueryService userQueryService;
    private final AddressQueryService addressQueryService;

    private final PaymentCommandService paymentCommandService;
    private final ReservationCommandService reservationCommandService;

    private final CouponQueryService couponQueryService;
    private final CouponCommandService couponCommandService;

    private final ConcertQueryService concertQueryService;



    @Override
    public ConfirmResponse processPayment(final PaymentCommand command) {

        CouponDetailResponse couponResponse = couponQueryService.getCouponDetailById(command.userCouponId);


        // Payment & Reservation 생성
        Payment payment = Payment.builder()
                .totalPrice(command.totalAmount)
                .paymentStatus(PaymentStatus.COMPLETED)
                .expiryAt(command.approvedAt)
                .discountRate(couponResponse.discountRate())
                .build();

        User user = userQueryService.getUserByUserId(command.userId);
        Address address = addressQueryService.getAddressByAddressId(command.addressId);

        Reservation reservation = Reservation.builder()
                .user(user)
                .address(address)
                .payment(payment)
                .status(ReservationStatus.ORDERED)
                .build();


        paymentCommandService.savePayment(payment);
        Reservation savedReservation = reservationCommandService.saveReservation(reservation);

        couponCommandService.useUserCoupon(command.userCouponId);
        ConcertResponse concert = concertQueryService.findById(command.concertId);
        // 반환값 생성

        ConfirmResponse response = ConfirmResponse.builder()
                .userId(command.userId)
                .reservationId(savedReservation.getId())
                .concludePrice(command.totalAmount)
                .status(savedReservation.getStatus())
                .concertTitle(concert.getTitle())
                .concertStartHour(concert.getStartHour())
                .seatsId(command.seatIds)
                .build();

        return response;
    }
}
