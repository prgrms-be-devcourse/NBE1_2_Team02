package com.example.book_your_seat.concert.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultRedisConcert {

    private List<ConcertListResponse> concertList = new ArrayList<>();
}

