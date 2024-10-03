package com.example.book_your_seat.reservation.service.facade.impl;

import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.facade.CouponCommandService;
import com.example.book_your_seat.payment.controller.dto.PaymentDto;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.service.facade.PaymentService;
import com.example.book_your_seat.payment.service.tossPayment.TossPaymentService;
import com.example.book_your_seat.reservation.controller.dto.ConfirmationReservationRequest;
import com.example.book_your_seat.reservation.controller.dto.ConfirmationReservationResponse;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.command.ReservationCommandService;
import com.example.book_your_seat.reservation.service.facade.ReservationService;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.facade.SeatService;
import com.example.book_your_seat.seat.service.redis.SeatRedisService;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final TossPaymentService tossPaymentService;
    private final SeatRedisService seatRedisService;
    private final CouponCommandService couponCommandService;
    private final PaymentService paymentService;
    private final UserQueryService userQueryService;
    private final SeatService seatService;
    private final ReservationCommandService reservationCommandService;

    @Override
    public CompletableFuture<ConfirmationReservationResponse> confirmationReservation(ConfirmationReservationRequest request) {
        // TossPaymentService의 비동기 메서드를 호출
        CompletableFuture<TossPaymentConfirmSuccessResponse> confirmFuture =
                tossPaymentService.requestConfirm(TossPaymentConfirmRequest.from(request));

        // 좌석 검증
        seatRedisService.validateSeat(request); // 좌석 결재가능 시간 초과 검증
        UserCoupon userCoupon = couponCommandService.userCoupon(request.couponId());
        User user = userQueryService.getUser(request.userId());
        Address address = userQueryService.getAddress(request.addressId());
        List<Seat> seats = seatService.getSeats(request.seatIds());

        // 결과를 비동기적으로 처리
        return confirmFuture.thenApply(confirmResponse -> {
            // confirmResponse를 사용하여 필요한 작업 수행
            Payment payment = paymentService.createPayment(PaymentDto.from(confirmResponse, userCoupon.getCoupon().getDiscountRate()));
            Reservation reservation = reservationCommandService.createReservation(user, address, payment, request.couponId(), seats);
            seatService.seatReservationComplete(seats, reservation);
            seatRedisService.deleteCache(request.seatIds());

            // 결과를 반환
            return new ConfirmationReservationResponse(
                    reservation.getId(),
                    reservation.getStatus(),
                    reservation.getFinalPrice(),
                    address.getDetail(),
                    seats
            );
        }).exceptionally(ex -> {
            // 예외 처리
            log.error("Payment confirmation failed: {}", ex.getMessage());
            throw new RuntimeException("Payment confirmation failed", ex); // 예외 발생
        });
    }
}
