package com.example.book_your_seat.concert.service;

import com.example.book_your_seat.concert.controller.dto.ConcertResponse;

import java.util.List;

public interface ConcertQueryService {

    List<ConcertResponse> findAll();
    ConcertResponse findById(Long id);
}
