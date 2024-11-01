package com.example.book_your_seat.likeconcert.repository;

import com.example.book_your_seat.likeconcert.domain.LikeConcert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeConcertRepository extends JpaRepository<LikeConcert, Long>, LikeConcertRepositoryCustom {

    boolean existsByUserIdAndConcertId(Long userId, Long concertId);
    void deleteByUserIdAndConcertId(Long userId, Long concertId);
}
