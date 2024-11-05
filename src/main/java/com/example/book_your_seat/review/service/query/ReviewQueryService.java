package com.example.book_your_seat.review.service.query;

import com.example.book_your_seat.review.controller.dto.ReviewListResponse;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;



    public List<ReviewListResponse> reviewPageList(Long concertId, Long reviewId, Pageable pageable){
        return reviewRepository.pageNationReviewList(concertId, reviewId, pageable).stream()
                .toList();
    }

    public List<ReviewListResponse> reviewUserList(Long userId, Long reviewId, Pageable pageable){
        return reviewRepository.pageNationUserReviewList(userId, reviewId, pageable).stream()
                .toList();
    }






}
