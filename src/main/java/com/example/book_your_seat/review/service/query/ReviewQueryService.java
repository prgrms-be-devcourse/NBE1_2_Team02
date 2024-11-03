package com.example.book_your_seat.review.service.query;

import com.example.book_your_seat.review.controller.dto.ReviewResDTO;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.review.ReviewConst.NOT_FOUND_REVIEW;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;



    public List<ReviewResDTO> reviewPageList(Long reviewId, Long concertId, Pageable pageable){
        Slice<Review> reviews = reviewRepository.pageNationReviewList(reviewId, concertId, pageable);

        return reviews.stream().map(ReviewResDTO::fromReview).toList();
    }

    public List<ReviewResDTO> reviewUserList(Long userId, Long reviewId, Pageable pageable){
        Slice<Review> reviews = reviewRepository.pageNationUserReviewList(reviewId, userId, pageable);

        return reviews.stream().map(ReviewResDTO::fromReview).toList();
    }






}
