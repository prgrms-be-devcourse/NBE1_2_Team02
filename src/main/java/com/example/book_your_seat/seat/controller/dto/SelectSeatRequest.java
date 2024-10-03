package com.example.book_your_seat.seat.controller.dto;

import java.util.List;

public record SelectSeatRequest(

        Long addressId,
        List<Long> seatIds

) {
}
