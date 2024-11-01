package com.example.book_your_seat.review.repository;

import com.example.book_your_seat.review.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryCustom {

    Slice<Review> pageNationReviewList(Long reviewId, Long concertId, Pageable pageable);
}
