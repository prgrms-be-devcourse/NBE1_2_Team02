package com.example.book_your_seat.seat.controller.dto;

import com.example.book_your_seat.seat.domain.Seat;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public final class SeatResponse {

    private final Long concertId;
    private final List<Long> seatNumbers;

    private SeatResponse(Long concertId, List<Long> seatNumbers) {
        this.concertId = concertId;
        this.seatNumbers = seatNumbers;
    }

    public static SeatResponse from(final List<Seat> seats) {
        Long concertId = seats.get(0).getId().getConcertId();
        List<Long> seatNumbers = seats.stream()
                .map(seat -> seat.getId().getSeatNumber())
                .toList();

        return new SeatResponse(concertId, seatNumbers);
    }
}
