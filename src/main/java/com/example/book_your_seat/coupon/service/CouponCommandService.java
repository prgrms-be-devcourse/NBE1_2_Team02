package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponIdResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;
import com.example.book_your_seat.user.domain.User;

public interface CouponCommandService {
    UserCouponIdResponse issueCouponWithPessimistic(User user, Long couponId);
    UserCouponIdResponse issueCouponWithOptimistic(User user, Long couponId);
    CouponIdResponse createCoupon(CouponCreateRequest couponCreateRequest);

}
