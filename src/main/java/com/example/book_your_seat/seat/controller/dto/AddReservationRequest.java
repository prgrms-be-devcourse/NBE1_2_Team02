package com.example.book_your_seat.seat.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static com.example.book_your_seat.seat.SeatConst.*;

public record AddReservationRequest(

        @NotNull(message = ENTER_USER_ID)
        Long userId,

        @NotNull(message = ENTER_CONCERT_ID)
        Long concertId,

        @NotNull(message = ENTER_ADDRESS_ID)
        Long addressId,

        @NotNull(message = ENTER_SEATS)
        List<Long> seats

) {
}
