package com.example.book_your_seat.concert.service;

import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;

public interface ConcertCommandService {

    ConcertResponse add(AddConcertRequest request);
    void delete(Long id);
}
