package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.ConcertReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertReservationRepository extends JpaRepository<ConcertReservation, Long> {
}
