package com.example.book_your_seat.coupon.controller.dto;

import com.example.book_your_seat.coupon.domain.Coupon;
import java.time.LocalDate;

public record CouponDetailResponse(
        String discountRate,
        LocalDate issuedAt
) {

    public static CouponDetailResponse fromCoupon(Coupon coupon) {
        return new CouponDetailResponse(
                coupon.getDiscountRate().getStringForm(),
                coupon.getCreatedAt().toLocalDate()
        );
    }
}
