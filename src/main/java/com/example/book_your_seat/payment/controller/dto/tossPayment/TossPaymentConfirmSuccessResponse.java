package com.example.book_your_seat.payment.controller.dto.tossPayment;

public record TossPaymentConfirmSuccessResponse(
        String orderId,
        Integer totalAmount
) {
}
