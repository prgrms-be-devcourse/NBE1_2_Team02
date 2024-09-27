package com.example.book_your_seat.reservation.service;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.reservation.controller.dto.AddReservationRequest;
import com.example.book_your_seat.reservation.controller.dto.ReservationResponse;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.reservation.ReservationConst.INVALID_COUPON;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Override
    public ReservationResponse bookReservation(AddReservationRequest request) {
/*        Long userCouponId = request.userCouponId();
        couponRepository.findById(couponId).*/

        // 쿠폰 할인율 찾아 -> concert 에서 price 를 조회 후 적용
        // coupon status 변경
        // seat status 변경
        // reservation 생성 후 저장
        request

        couponRepository.findById(coupon.getId())
                .ifPresentOrElse(
                        savedCoupon -> { },// 쿠폰 감소 로직 추가
                        () -> new IllegalArgumentException(INVALID_COUPON)
                );
        DiscountRate discountRate = coupon.getDiscountRate();

        return null;
    }

    @Override
    public void cancelReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
