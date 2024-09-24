package com.example.book_your_seat.concert.controller.dto;

import com.example.book_your_seat.concert.domain.Concert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

import static com.example.book_your_seat.concert.ConcertConst.TOTAL_STOCK;

public record AddConcertRequest(

        @NotBlank
        String title,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        @NotNull
        Integer price,

        @NotNull
        Integer time
) {
        public static Concert to(final AddConcertRequest request) {
                return new Concert(request.title, TOTAL_STOCK,
                        request.startDate, request.endDate, request.price, request.time);
        }
}
