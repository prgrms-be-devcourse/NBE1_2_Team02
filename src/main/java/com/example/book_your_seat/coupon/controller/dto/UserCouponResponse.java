package com.example.book_your_seat.coupon.controller.dto;

import com.example.book_your_seat.coupon.domain.DiscountRate;

import java.time.LocalDate;

public record UserCouponResponse(
        Long userCouponId,
        DiscountRate discountRate,
        LocalDate expirationDate,
        Boolean isUsed
) {
}
