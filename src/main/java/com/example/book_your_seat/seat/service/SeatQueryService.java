package com.example.book_your_seat.seat.service;

import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public interface SeatQueryService {

    List<Seat> findAllSeats(Long concertId);
}