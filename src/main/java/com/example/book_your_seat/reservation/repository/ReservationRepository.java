package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationQueryRepository {

    Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId);

}
