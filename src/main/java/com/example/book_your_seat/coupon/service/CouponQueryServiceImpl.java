package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.coupon.CouponConst.INVALID_COUPON_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryServiceImpl implements CouponQueryService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Override
    public Coupon findByIdWithPessimistic(Long couponId) {
        return couponRepository.findByIdWithPessimistic(couponId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_COUPON_ID + couponId));
    }

    @Override
    public Coupon findByIdWithOptimistic(Long couponId) {
        return couponRepository.findByIdWithOptimistic(couponId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_COUPON_ID + couponId));
    }

    @Override
    public List<UserCouponResponse> getUserCoupons(User user) {
        return userCouponRepository.findAllByUser(user).stream()
                .map(userCoupon ->
                        new UserCouponResponse(
                                userCoupon.getId(),
                                userCoupon.getCoupon().getDiscountRate(),
                                userCoupon.getCoupon().getExpirationDate(),
                                userCoupon.isUsed())
                ).toList();
    }
}
