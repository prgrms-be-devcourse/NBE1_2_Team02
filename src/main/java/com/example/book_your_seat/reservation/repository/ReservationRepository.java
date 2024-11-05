package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.seat.domain.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @Query(value = "SELECT r.user_id FROM Reservation r WHERE r.status = 'SHIPPED' AND r.last_modified_at BETWEEN :startDate AND :endDate ORDER BY RAND() LIMIT 50", nativeQuery = true)
    List<Long> findShippedReservationsLastMonth(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    boolean existsByStatusIs(ReservationStatus status);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r JOIN r.seatIds s WHERE s.concertId = :concertId AND s.seatNumber = :seatNumber")
    boolean existsBySeatId(@Param("concertId") Long concertId, @Param("seatNumber") Long seatNumber);
}
