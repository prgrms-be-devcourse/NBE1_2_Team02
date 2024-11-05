package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public interface SeatJdbcRepository {

    void insertBulkSeats(List<Seat> seats);
}
