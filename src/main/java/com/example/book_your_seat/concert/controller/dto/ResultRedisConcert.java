package com.example.book_your_seat.concert.controller.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResultRedisConcert {

    private List<ConcertListResponse> concertList = new ArrayList<>();

    public ResultRedisConcert(List<ConcertListResponse> concertList) {
        this.concertList = concertList;
    }

    public ResultRedisConcert() {
    }
}
