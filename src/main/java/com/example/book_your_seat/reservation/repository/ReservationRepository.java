package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.payment p " +
            "JOIN FETCH r.seats s " +
            "WHERE r.id= :reservationId")
    Reservation findReservationWithPaymentAndSeats(@Param("reservationId") Long reservationId);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.payment p " +
            "WHERE r.id= :reservationId")
    Reservation findReservationWithPayment(@Param("reservationId") Long reservationId);


    @Query(value = "SELECT r.user_id FROM Reservation r WHERE r.status = 'SHIPPED' AND r.last_modified_at BETWEEN :startDate AND :endDate ORDER BY RAND() LIMIT 50", nativeQuery = true)
    List<Long> findShippedReservationsLastMonth(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
