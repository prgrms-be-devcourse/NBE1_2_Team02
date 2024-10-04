package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.seat.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatQueryRepository {

    @Query(value = "SELECT * FROM Seat WHERE concert_id = :concertId", nativeQuery = true)
    List<Seat> findByConcertId(@Param("concertId") Long concertId);

    List<Seat> findByConcert(Concert concert);
}

