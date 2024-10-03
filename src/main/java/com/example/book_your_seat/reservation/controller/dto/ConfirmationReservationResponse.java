package com.example.book_your_seat.reservation.controller.dto;

import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public record ConfirmationReservationResponse(
        Long reservationId,
        ReservationStatus status,
        Integer finalPrice,
        String address,
        List<Seat> seats
) {
}
