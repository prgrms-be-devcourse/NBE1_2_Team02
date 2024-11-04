package com.example.book_your_seat.review.controller.dto;

import com.example.book_your_seat.review.domain.Review;

public record ReviewListResponse(String username, String content, int startCount, Long reviewId) {


    public static ReviewListResponse fromReview(Review review) {
        return new ReviewListResponse(review.getUser().getUsername(), review.getContent(), review.getStarCount(), review.getId());
    }
}