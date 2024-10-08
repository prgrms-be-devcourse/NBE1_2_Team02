package com.example.book_your_seat.seat.service.query;

import com.example.book_your_seat.seat.domain.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeatQueryService {

    List<Seat> findAllSeats(Long concertId);

    List<Integer> findSeatNumbers(List<Long> seatIds);

    Integer getSeatPrice(final Long SeatId);

    List<Seat> getSeats(final List<Long> seatIds);

}
