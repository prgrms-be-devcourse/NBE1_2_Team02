package com.example.book_your_seat.review.controller.dto;

import com.example.book_your_seat.review.domain.Review;

public record ReviewResponse(Long id) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(review.getId());
    }
}
