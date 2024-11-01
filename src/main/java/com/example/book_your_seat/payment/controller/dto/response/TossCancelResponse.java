package com.example.book_your_seat.payment.controller.dto.response;

public record TossCancelResponse(
        String status,
        Long totalAmount
) {
}
