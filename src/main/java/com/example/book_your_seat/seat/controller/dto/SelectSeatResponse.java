package com.example.book_your_seat.seat.controller.dto;

import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public record SelectSeatResponse(

        List<Long> seatId
) {
    public static SelectSeatResponse fromSeats(final List<Seat> seats) {
        return new SelectSeatResponse(seats.stream().map(Seat::getId).toList());
    }
}
