package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID>, ReservationQueryRepository {

    Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId);

}
