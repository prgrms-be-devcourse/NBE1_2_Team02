package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatQueryRepository {

    List<Seat> findValidSeats(List<Long> seats);

}