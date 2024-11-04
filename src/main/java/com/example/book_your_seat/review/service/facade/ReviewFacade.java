package com.example.book_your_seat.review.service.facade;


import com.example.book_your_seat.review.controller.dto.ReviewListResponse;
import com.example.book_your_seat.review.controller.dto.ReviewResponse;
import com.example.book_your_seat.review.service.command.ReviewCommandService;
import com.example.book_your_seat.review.service.query.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewFacade {

    private final ReviewCommandService reviewCommandService;

    private final ReviewQueryService reviewQueryService;



    public ReviewResponse saveReview(Long userId, Long concertId, String content, int starCount){
        return reviewCommandService.createReview(userId, concertId, content, starCount);
    }

    public ReviewResponse updateReview(Long userId, Long reviewId, String content, int starCount){
        return reviewCommandService.updateReview(userId, reviewId, content, starCount);
    }

    public void deleteReview(Long userId, Long concertId){
        reviewCommandService.deleteReview(userId, concertId);
    }


    public List<ReviewListResponse> pageNationReview(Long concertId, Long reviewId, Pageable pageable){
        return reviewQueryService.reviewPageList(concertId, reviewId, pageable);
    }

    public List<ReviewListResponse> pageNationUserReview(Long userId, Long reviewId, Pageable pageable){
        return reviewQueryService.reviewUserList(userId, reviewId, pageable);
    }



}
