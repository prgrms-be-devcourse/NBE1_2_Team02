package com.example.book_your_seat.coupon.service.query;

import static com.example.book_your_seat.coupon.CouponConst.COUPON_NOT_FOUND;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryService {

    private final UserCouponQueryService userCouponQueryService;
    private final CouponRepository couponRepository;

    public CouponDetailResponse getCouponDetailById(Long userCouponId) {
        UserCoupon userCoupon = userCouponQueryService.findValidUserCoupon(userCouponId);
        Coupon coupon = userCoupon.getCoupon();
        return CouponDetailResponse.fromCoupon(coupon);
    }

    public DiscountRate getDiscountRate(Long userCouponId) {
        return userCouponQueryService.findValidUserCoupon(userCouponId)
                .getCoupon()
                .getDiscountRate();
    }

    public Coupon findByIdWithPessimistic(Long couponId) {
        return couponRepository.findByIdWithPessimistic(couponId)
                .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_FOUND));
    }
}
