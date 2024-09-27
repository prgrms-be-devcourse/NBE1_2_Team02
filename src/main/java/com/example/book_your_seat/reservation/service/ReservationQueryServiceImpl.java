package com.example.book_your_seat.reservation.service;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.reservation.controller.dto.ReservationResponse;
import com.example.book_your_seat.reservation.controller.dto.SimpleReservationResponse;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.reservation.service.command.ReservationsCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.reservation.ReservationConst.INVALID_COUPON;
import static com.example.book_your_seat.reservation.ReservationConst.INVALID_RESERVATION;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Override
    public List<SimpleReservationResponse> findAllReservations(ReservationsCommand command) {
        return reservationRepository.findAllReservations(command)
                .stream()
                .map(SimpleReservationResponse::from)
                .toList();
    }

    @Override
    public ReservationResponse findReservationById(Long userId, Long reservationId) {

        Reservation reservation
                = reservationRepository.findByIdAndUserId(reservationId, userId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_RESERVATION));

        Long userCouponId = reservation.getUserCouponId();
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_COUPON));

        Coupon coupon = userCoupon.getCoupon();
        return ReservationResponse.from(reservation, coupon);
    }
}
