package com.example.book_your_seat.coupon.controller.dto;

import java.time.LocalDate;

public record CouponDetailResponse(
        String discountRate,
        LocalDate issuedAt
) {
}
