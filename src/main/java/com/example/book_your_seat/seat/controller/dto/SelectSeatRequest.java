package com.example.book_your_seat.seat.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static com.example.book_your_seat.seat.SeatConst.ENTER_SEATS;

public record SelectSeatRequest(

        @NotNull(message = ENTER_SEATS)
        List<Long> seatIds
) {
}
