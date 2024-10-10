package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.manager.UserCouponManager;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryFacade implements CouponQueryService {

    private final UserCouponManager userCouponManager;

    @Override
    public CouponDetailResponse getCouponDetailById(Long userCouponId) {
        UserCoupon userCoupon = userCouponManager.findValidUserCoupon(userCouponId);
        Coupon coupon = userCoupon.getCoupon();
        return CouponDetailResponse.fromCoupon(coupon);
    }

    @Override
    public DiscountRate getDiscountRate(Long userCouponId) {
        return userCouponManager.findValidUserCoupon(userCouponId)
                .getCoupon()
                .getDiscountRate();
    }
}
