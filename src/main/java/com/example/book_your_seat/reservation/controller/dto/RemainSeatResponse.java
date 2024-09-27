package com.example.book_your_seat.reservation.controller.dto;

import com.example.book_your_seat.seat.domain.Seat;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class RemainSeatResponse {

    private final List<Seat> seats;

}
