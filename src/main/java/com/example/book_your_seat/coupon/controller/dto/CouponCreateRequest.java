package com.example.book_your_seat.coupon.controller.dto;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;

public record CouponCreateRequest (
        int quantity,
        int discountRate
) {
    public Coupon toCoupon() {
        return new Coupon(
                this.quantity,
                DiscountRate.findBy(this.discountRate)
        );
    }
}
