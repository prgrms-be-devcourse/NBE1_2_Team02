package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
