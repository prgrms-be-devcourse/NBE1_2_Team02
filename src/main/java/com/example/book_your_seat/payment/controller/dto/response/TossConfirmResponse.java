package com.example.book_your_seat.payment.controller.dto.response;

import java.time.LocalDateTime;

public record TossConfirmResponse(
        String orderId,
        Long totalAmount,
        String paymentKey,
        LocalDateTime approvedAt
) {
}
