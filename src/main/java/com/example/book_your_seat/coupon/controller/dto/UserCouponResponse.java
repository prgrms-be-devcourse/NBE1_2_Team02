package com.example.book_your_seat.coupon.controller.dto;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserCouponResponse {

    private Long couponId;

    private boolean isUsed;

    private String expirationDate;

    private String discountRate;



    @QueryProjection
    public UserCouponResponse(Long couponId, boolean isUsed, LocalDate expirationDate, DiscountRate discountRate) {
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.expirationDate = expirationDate.toString();
        this.discountRate = discountRate.getStringForm();
    }
}
