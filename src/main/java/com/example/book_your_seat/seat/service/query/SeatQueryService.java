package com.example.book_your_seat.seat.service.query;

import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;

import java.util.List;

public interface SeatQueryService {
    List<RemainSeatResponse> findRemainSeats(Long concertId);

}
