package com.example.book_your_seat.seat.service.query;

import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public interface SeatQueryService {
    List<RemainSeatResponse> findRemainSeats(final Long concertId);

    Integer getSeatPrice(final Long SeatId);

    List<Seat> getSeats(final List<Long> seatIds);
}
