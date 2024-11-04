package com.example.book_your_seat.review.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import static com.example.book_your_seat.concert.ConcertConst.NOT_NULL_CONCERT_ID;
import static com.example.book_your_seat.review.ReviewConst.*;

public record ReviewUpdateDTO(

        @NotNull(message = NOT_EMPTY_ID)
        Long reviewId,

        @NotEmpty(message = NOT_CONTENT)
        String content,

        @NotNull(message = NOT_STAR_COUNT)
        int startCount

) {
}
