package com.example.book_your_seat.seat.controller.dto;

import com.example.book_your_seat.seat.domain.Seat;
import lombok.Getter;

import java.util.List;

@Getter
public final class SelectSeatResponse {

    private final Long concertId;
    private final List<Long> seatNumbers;

    private SelectSeatResponse(Long concertId, List<Long> seatNumbers) {
        this.concertId = concertId;
        this.seatNumbers = seatNumbers;
    }

    public static SelectSeatResponse fromSeats(final List<Seat> seats) {
        Long concertId = seats.get(0).getId().getConcertId();
        List<Long> seatNumbers = seats.stream()
                .map(seat -> seat.getId().getConcertId())
                .toList();

        return new SelectSeatResponse(concertId, seatNumbers);
    }
}
