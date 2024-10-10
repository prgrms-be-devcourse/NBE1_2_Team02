package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.domain.DiscountRate;

public interface CouponQueryService {

    CouponDetailResponse getCouponDetailById(Long userCouponId);

    DiscountRate getDiscountRate(Long userCouponId);
}
