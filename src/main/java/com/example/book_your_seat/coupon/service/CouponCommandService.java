package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.controller.dto.CouponResponse;

public interface CouponCommandService {

    CouponResponse createCoupon(CouponCreateRequest couponCreateRequest);
    CouponResponse issueCoupon(Long userId, Long couponId);

}
