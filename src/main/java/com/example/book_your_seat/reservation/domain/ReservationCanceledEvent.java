package com.example.book_your_seat.reservation.domain;

import com.example.book_your_seat.seat.domain.SeatId;

import java.util.List;

public record ReservationCanceledEvent(
        Long paymentId,
        List<SeatId> seatId
) {

}
