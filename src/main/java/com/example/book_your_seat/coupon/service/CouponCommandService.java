package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.Dto.CouponResponse;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.user.domain.User;

public interface CouponCommandService {

    CouponResponse useCoupon(Long userId, Long couponId);

    void decreaseCoupon(Long couponId);

}
