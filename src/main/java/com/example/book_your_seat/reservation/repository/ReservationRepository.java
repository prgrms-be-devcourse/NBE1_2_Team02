package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r.user.id FROM Reservation r WHERE r.status = 'SHIPPED' AND r.lastModifiedAt BETWEEN :startDate AND :endDate order by r.user.id limit 50")
    List<Long> findShippedReservationsLastMonth(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
