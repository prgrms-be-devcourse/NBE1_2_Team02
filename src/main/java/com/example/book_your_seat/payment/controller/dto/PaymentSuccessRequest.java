package com.example.book_your_seat.payment.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static com.example.book_your_seat.payment.PaymentConst.*;

public record PaymentSuccessRequest(

        @NotNull(message = ENTER_PAYMENTKEY)
        String paymentKey,
        @NotNull(message = ENTER_ORDER_ID)
        String orderId,
        @NotNull(message = ENTER_AMOUNT)
        Integer amount,
        @NotNull(message = ENTER_SEAT_ID)
        List<Long> seatIds,
        @NotNull(message = ENTER_ADDRESS_ID)
        Long addressId,
        @NotNull(message = ENTER_USER_ID)
        Long userId,
        Long couponId
) {
}
