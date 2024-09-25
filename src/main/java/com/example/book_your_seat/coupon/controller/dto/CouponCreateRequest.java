package com.example.book_your_seat.coupon.controller.dto;

import static com.example.book_your_seat.coupon.CouponConst.NULL_AMOUNT_MESSAGE;
import static com.example.book_your_seat.coupon.CouponConst.NULL_DISCOUNT_RATE_MESSAGE;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import jakarta.validation.constraints.NotNull;

public record CouponCreateRequest (

        @NotNull(message = NULL_AMOUNT_MESSAGE)
        int quantity,

        @NotNull(message = NULL_DISCOUNT_RATE_MESSAGE)
        int discountRate
) {
    public Coupon toCoupon() {
        return new Coupon(
                this.quantity,
                DiscountRate.findBy(this.discountRate)
        );
    }
}
