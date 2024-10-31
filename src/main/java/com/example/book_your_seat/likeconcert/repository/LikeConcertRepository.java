package com.example.book_your_seat.likeconcert.repository;

import com.example.book_your_seat.likeconcert.domain.LikeConcert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeConcertRepository extends JpaRepository<LikeConcert, Long> {

    List<LikeConcert> findByUserId(Long userId);

}
