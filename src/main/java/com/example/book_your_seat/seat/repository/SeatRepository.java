package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("SELECT s FROM Seat s WHERE s.concert.id = :concertId AND s.isSold = false")
    List<Seat> findByConcertIdAndNotSold(Long concertId);
}
