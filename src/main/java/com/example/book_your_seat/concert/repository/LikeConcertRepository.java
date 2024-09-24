package com.example.book_your_seat.concert.repository;

import com.example.book_your_seat.concert.domain.LikeConcert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeConcertRepository extends JpaRepository<LikeConcert, Long> {
}
