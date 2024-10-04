package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(value = "SELECT * FROM Seat WHERE concert_id = :concertId", nativeQuery = true)
    List<Seat> findByConcertId(Long concertId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    List<Seat> findAllByIdWithLock(List<Long> seatIds);

    @Query("SELECT s FROM Seat  s WHERE s.id IN :seatIds")
    List<Seat> findAllById(List<Long> seatIds);

}
