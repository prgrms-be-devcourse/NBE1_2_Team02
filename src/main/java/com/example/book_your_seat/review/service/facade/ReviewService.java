package com.example.book_your_seat.review.service.facade;


import com.example.book_your_seat.review.controller.dto.ReviewCreateResDTO;
import com.example.book_your_seat.review.controller.dto.ReviewResDTO;
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
public class ReviewService {

    private final ReviewCommandService reviewCommandService;

    private final ReviewQueryService reviewQueryService;



    public ReviewCreateResDTO saveReview(Long userId, Long concertId, String content, int startCount){

        return reviewCommandService.createReview(userId, concertId, content, startCount);
    }


    public List<ReviewResDTO> pageNationReview(Long reviewId, Long concertId, Pageable pageable){
        return reviewQueryService.reviewPageList(reviewId, concertId, pageable);
    }

    public List<ReviewResDTO> pageNationUserReview(Long reviewId, Long userId, Pageable pageable){
        return reviewQueryService.reviewUserList(userId, reviewId, pageable);
    }
}
