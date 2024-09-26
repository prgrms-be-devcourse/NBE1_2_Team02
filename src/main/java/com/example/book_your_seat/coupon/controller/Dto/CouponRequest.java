package com.example.book_your_seat.coupon.controller.Dto;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.example.book_your_seat.coupon.CouponConst.*;

public record CouponRequest (

    @NotNull(message = QUANTITY_COUPON)
    Integer amount,

    @NotNull(message = DISCOUNT_COUPON)
    DiscountRate discountRate,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dateTime

    ){

    public static Coupon to(CouponRequest couponRequest){
        return new Coupon(couponRequest.amount, couponRequest.discountRate, couponRequest.dateTime);
    }
}
