package com.example.book_your_seat.reservation.contorller.dto.request;

import com.example.book_your_seat.reservation.ReservationConst;
import jakarta.validation.constraints.NotNull;

public record CancelReservationRequest(
        @NotNull(message = ReservationConst.ENTERED_RESERVATION_NUMBER)
        Long ReservationId
) {
}
