package com.example.book_your_seat.review.repository;

import com.example.book_your_seat.review.controller.dto.ReviewListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryCustom {

    Slice<ReviewListResponse> pageNationReviewList(Long concertId, Long reviewId, Pageable pageable);

    Slice<ReviewListResponse> pageNationUserReviewList(Long userId, Long reviewId, Pageable pageable);
}
