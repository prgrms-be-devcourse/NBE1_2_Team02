package com.example.book_your_seat.concert.controller.dto;

import com.example.book_your_seat.concert.domain.Concert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

import static com.example.book_your_seat.concert.ConcertConst.*;

public record AddConcertRequest(

        @NotBlank(message = ENTER_CONCERT_TITLE)
        String title,

        @NotNull(message = ENTER_CONCERT_START_DATE)
        LocalDate startDate,

        @NotNull(message = ENTER_CONCERT_END_DATE)
        LocalDate endDate,

        @NotNull(message = ENTER_CONCERT_PRICE)
        Integer price,

        @NotNull(message = ENTER_CONCERT_RUNNING_TIME)
        Integer startHour
) {
        public static Concert to(final AddConcertRequest request) {
                return new Concert(
                        request.title,
                        request.startDate,
                        request.endDate,
                        request.price,
                        request.startHour
                );
        }
}
