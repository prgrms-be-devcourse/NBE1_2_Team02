package com.example.book_your_seat.payment.controller.dto;

public record TossPaymentConfirmErrorResponse(
        String code,
        String message
) {
}
