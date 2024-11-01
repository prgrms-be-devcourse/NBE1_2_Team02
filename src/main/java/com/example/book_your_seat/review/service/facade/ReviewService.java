package com.example.book_your_seat.review.service.facade;


import com.example.book_your_seat.review.controller.dto.ReviewCreateResDTO;
import com.example.book_your_seat.review.service.command.ReviewCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewCommandService reviewCommandService;


    public ReviewCreateResDTO saveReview(Long userId, Long concertId, String content, int startCount){

        return reviewCommandService.createReview(userId, concertId, content, startCount);
    }
}
