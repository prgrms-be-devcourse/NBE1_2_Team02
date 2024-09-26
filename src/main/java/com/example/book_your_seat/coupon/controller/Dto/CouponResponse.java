package com.example.book_your_seat.coupon.controller.Dto;

import com.example.book_your_seat.coupon.domain.Coupon;
import lombok.Getter;

@Getter
public class CouponResponse {

    private Long id;

    public CouponResponse(Long id) {
        this.id = id;
    }

    public static CouponResponse fromDto(Coupon coupon) {
        return new CouponResponse(coupon.getId());
    }

}
