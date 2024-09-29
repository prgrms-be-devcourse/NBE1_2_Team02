package com.example.book_your_seat.payment.controller.dto;

public record TossPaymentConfirmSuccessResponse(
        String orderId,
        Integer totalAmount
) {
}
