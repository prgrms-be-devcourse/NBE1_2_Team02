package com.example.book_your_seat.review.service.query;

import com.example.book_your_seat.review.controller.dto.ReviewResDTO;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.review.ReviewConst.NOT_FOUND_REVIEW;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;


    public List<ReviewResDTO> reviewFindAll(Long concertId){
        List<Review> reviews = reviewRepository.findAllByConcertId(concertId).orElseThrow(()-> new IllegalArgumentException(NOT_FOUND_REVIEW));

        return reviews.stream().map(ReviewResDTO::fromReview).toList();

    }


    public List<ReviewResDTO> userReviewAll(Long userId){
        List<Review> reviews = reviewRepository.findAllByUserId(userId).orElseThrow(()-> new IllegalArgumentException(NOT_FOUND_REVIEW));

        return reviews.stream().map(ReviewResDTO::fromReview).toList();
    }






}
