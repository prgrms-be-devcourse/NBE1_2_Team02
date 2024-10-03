package com.example.book_your_seat.seat.service.facade;

import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;

import java.util.List;

public interface SeatService {
    SelectSeatResponse selectSeat(final SelectSeatRequest request);

    SelectSeatResponse selectSeatRedisson(final SelectSeatRequest request);

    List<RemainSeatResponse> findRemainSeats(final Long concertId);
}
