package com.example.book_your_seat.coupon.controller.dto;

import jakarta.validation.constraints.NotNull;


public record UserCouponRequest(
        Boolean used
) {


}
