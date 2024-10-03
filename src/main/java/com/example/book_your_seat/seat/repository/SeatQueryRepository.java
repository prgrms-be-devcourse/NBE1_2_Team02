package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatQueryRepository {

    List<Seat> findValidSeats(Long concertId, List<Long> seats);
    Long reserveSeats(Long concertId, List<Long> seatId);
    void cancelSeats(List<Long> seatId);

}
