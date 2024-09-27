package com.example.book_your_seat.coupon.controller.dto;

import static com.example.book_your_seat.coupon.CouponConst.INVALID_EXPIRATION_DATE_MESSAGE;
import static com.example.book_your_seat.coupon.CouponConst.NULL_AMOUNT_MESSAGE;
import static com.example.book_your_seat.coupon.CouponConst.NULL_DISCOUNT_RATE_MESSAGE;
import static com.example.book_your_seat.coupon.CouponConst.NULL_EXPIRATION_DATE_MESSAGE;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CouponCreateRequest (

        @NotNull(message = NULL_AMOUNT_MESSAGE)
        int quantity,

        @NotNull(message = NULL_DISCOUNT_RATE_MESSAGE)
        int discountRate,

        @NotNull(message = NULL_EXPIRATION_DATE_MESSAGE)
        @Future(message = INVALID_EXPIRATION_DATE_MESSAGE)
        LocalDate expirationDate
) {
    public Coupon toCoupon() {
        return new Coupon(
                this.quantity,
                DiscountRate.findBy(this.discountRate),
                this.expirationDate
        );
    }
}
