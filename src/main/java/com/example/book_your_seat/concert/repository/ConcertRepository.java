package com.example.book_your_seat.concert.repository;

import com.example.book_your_seat.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
