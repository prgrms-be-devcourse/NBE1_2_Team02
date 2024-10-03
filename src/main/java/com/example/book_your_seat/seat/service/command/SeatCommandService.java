package com.example.book_your_seat.seat.service.command;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public interface SeatCommandService {
    List<Seat> selectSeat(final SelectSeatRequest request);

    void cacheSeatIds(final List<Seat> seats);

    List<Seat> selectSeatRedisson(final SelectSeatRequest request);
}
