package com.example.book_your_seat.payment.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static com.example.book_your_seat.payment.PaymentConst.ENTER_ORDER_ID;

public record FinalPriceRequest(
        @NotNull(message = ENTER_ORDER_ID)
        List<Long> seatIds,

        Long userCouponId
) {
}
