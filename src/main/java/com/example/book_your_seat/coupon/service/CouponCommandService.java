package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;
import com.example.book_your_seat.coupon.controller.dto.UserCouponIdResponse;

public interface CouponCommandService {

    CouponResponse createCoupon(CouponCreateRequest couponCreateRequest);
    UserCouponIdResponse issueCouponWithPessimistic(Long userId, Long couponId);

}
