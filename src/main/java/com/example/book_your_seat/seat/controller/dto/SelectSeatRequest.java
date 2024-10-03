package com.example.book_your_seat.seat.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static com.example.book_your_seat.seat.SeatConst.ENTER_SEATS;
import static com.example.book_your_seat.seat.SeatConst.ENTER_USER_ID;

public record SelectSeatRequest(
        @NotNull(message = ENTER_USER_ID)
        Long userId,
        @NotNull(message = ENTER_SEATS)
        List<Long> seatIds
) {
}
