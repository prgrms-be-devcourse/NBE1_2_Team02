package com.example.book_your_seat.payment.controller.dto.tossPayment;

import java.time.LocalDateTime;

public record TossPaymentConfirmSuccessResponse(
        String orderId,
        Integer totalAmount,
        String paymentKey,
        LocalDateTime approvedAt
) {
}
