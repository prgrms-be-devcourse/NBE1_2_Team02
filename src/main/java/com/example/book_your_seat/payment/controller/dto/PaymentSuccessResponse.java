package com.example.book_your_seat.payment.controller.dto;

import com.example.book_your_seat.reservation.domain.Reservation;

public record PaymentSuccessResponse(
        String orderId,
        Reservation reservation
) {
}
