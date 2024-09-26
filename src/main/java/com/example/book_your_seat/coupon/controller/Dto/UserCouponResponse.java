package com.example.book_your_seat.coupon.controller.Dto;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import lombok.Getter;

@Getter
public class UserCouponResponse {

    private String message;

    private DiscountRate discountRate;

    public UserCouponResponse(Coupon coupon, String message) {
        this.message = message;
        this.discountRate = coupon.getDiscountRate();
    }

    public static UserCouponResponse fromCoupon(Coupon coupon, String message) {
        return new UserCouponResponse(coupon, message);
    }
}
