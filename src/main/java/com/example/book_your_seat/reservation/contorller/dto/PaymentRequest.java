package com.example.book_your_seat.reservation.contorller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static com.example.book_your_seat.payment.PaymentConst.*;

public record PaymentRequest(

        @NotNull(message = ENTER_PAYMENTKEY)
        String paymentKey,
        @NotNull(message = ENTER_ORDER_ID)
        String orderId,
        @NotNull(message = ENTER_AMOUNT)
        Long amount,
        @NotNull(message = ENTER_SEAT_ID)
        List<Long> seatIds,
        @NotNull(message = ENTER_ADDRESS_ID)
        Long addressId,
        @NotNull(message = ENTER_USER_ID)
        Long userId,

        Long concertId,

        Long userCouponId
) {
}
