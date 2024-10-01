package com.example.book_your_seat.coupon.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserCouponResponse {

    private boolean isUsed;

    private String expirationDate;

    private String discountRate;

    @QueryProjection
    public UserCouponResponse(boolean isUsed, String expirationDate, String discountRate) {
        this.isUsed = isUsed;
        this.expirationDate = expirationDate;
        this.discountRate = discountRate;
    }
}
