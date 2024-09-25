package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.domain.Coupon;

public interface CouponQueryService {
    Coupon findByIdWithPessimistic(Long couponId);
}
