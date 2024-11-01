package com.example.book_your_seat.review.controller.dto;

import com.example.book_your_seat.review.domain.Review;

public record ReviewCreateResDTO(Long id) {

    public static ReviewCreateResDTO from(Review review) {
        return new ReviewCreateResDTO(review.getId());
    }
}
