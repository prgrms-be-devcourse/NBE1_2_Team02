package com.example.book_your_seat.payment.controller.dto.request;

import com.example.book_your_seat.reservation.contorller.dto.PaymentRequest;
import jakarta.validation.constraints.NotNull;

import static com.example.book_your_seat.payment.PaymentConst.*;

public record TossConfirmRequest(
        @NotNull(message = ENTER_PAYMENT_KEY)
        String paymentKey,
        @NotNull(message = ENTER_ORDER_ID)
        String orderId,
        @NotNull(message = ENTER_AMOUNT)
        Long amount
) {
    public static TossConfirmRequest from(PaymentRequest request) {
        return new TossConfirmRequest(
                request.paymentKey(),
                request.orderId(),
                request.amount()
        );
    }
}
