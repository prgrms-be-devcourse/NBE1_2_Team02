package com.example.book_your_seat.review.repository;

import com.example.book_your_seat.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    @Query("select r from Review r join fetch r.user where r.concert.id = :concertId")
    Optional<List<Review>> findAllByConcertId(@Param("concertId") Long concertId);

    @Query("select r from Review r join fetch r.user where r.user.id = :userId")
    Optional<List<Review>> findAllByUserId(@Param("userId") Long userId);
}
