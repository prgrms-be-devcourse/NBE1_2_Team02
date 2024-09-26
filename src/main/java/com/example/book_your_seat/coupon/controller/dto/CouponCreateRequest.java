package com.example.book_your_seat.coupon.controller.dto;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static com.example.book_your_seat.coupon.CouponConst.INVALID_COUPON_AMOUNT;

public record CouponCreateRequest(
        @NotNull
        @Min(value = 0, message = INVALID_COUPON_AMOUNT)
        int amount,
        @NotNull
        DiscountRate discountRate
) {
}
