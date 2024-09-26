package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.user.domain.User;

import java.util.List;

public interface CouponQueryService {
    Coupon findByIdWithPessimistic(Long couponId);
    Coupon findByIdWithOptimistic(Long couponId);
    List<UserCouponResponse> getUserCoupons(User user);
}
