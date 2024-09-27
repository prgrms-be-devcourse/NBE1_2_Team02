package com.example.book_your_seat.seat.controller.dto;

import com.example.book_your_seat.seat.domain.Seat;

public record RemainSeatResponse(
        Long seatId,

        Integer price
) {
        public static RemainSeatResponse from(final Seat seat) {
            return new RemainSeatResponse(seat.getId(), seat.getConcert().getPrice());
        }
}
