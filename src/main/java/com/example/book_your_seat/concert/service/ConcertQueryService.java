package com.example.book_your_seat.concert.service;

import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.controller.dto.ResultRedisConcert;

import java.util.List;

public interface ConcertQueryService {

    List<ConcertResponse> findAll();
    ConcertResponse findById(Long id);

    List<ConcertListResponse> findAllConcertList();

    ResultRedisConcert finduesdRedisAllConcertList();

}
