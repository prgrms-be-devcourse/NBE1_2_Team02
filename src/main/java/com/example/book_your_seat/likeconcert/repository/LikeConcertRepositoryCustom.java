package com.example.book_your_seat.likeconcert.repository;

import com.example.book_your_seat.likeconcert.domain.LikeConcert;

import java.util.List;

public interface LikeConcertRepositoryCustom {

    List<LikeConcert> findByUserId(Long userId, Long lastLikeId);
}
