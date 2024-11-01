package com.example.book_your_seat.review.controller.dto;

import com.example.book_your_seat.review.domain.Review;

public record ReviewResDTO(String username, String content, int startCount) {


    public static ReviewResDTO fromReview(Review review) {
        return new ReviewResDTO(review.getUser().getUsername(), review.getContent(), review.getStarCount());
    }
}
