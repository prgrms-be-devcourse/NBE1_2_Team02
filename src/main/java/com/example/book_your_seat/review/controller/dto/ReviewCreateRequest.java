package com.example.book_your_seat.review.controller.dto;

import jakarta.validation.constraints.*;

import static com.example.book_your_seat.concert.ConcertConst.NOT_NULL_CONCERT_ID;
import static com.example.book_your_seat.review.ReviewConst.NOT_CONTENT;
import static com.example.book_your_seat.review.ReviewConst.NOT_STAR_COUNT;

public record ReviewCreateRequest(

        @NotEmpty(message = NOT_CONTENT)
        String content,

        @NotNull(message = NOT_STAR_COUNT)
        int startCount,

        @NotNull(message = NOT_NULL_CONCERT_ID)
        Long concertId
) {


}
