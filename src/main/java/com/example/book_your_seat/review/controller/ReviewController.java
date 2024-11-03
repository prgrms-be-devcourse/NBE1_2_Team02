package com.example.book_your_seat.review.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.review.controller.dto.ReviewCreateReqDTO;
import com.example.book_your_seat.review.controller.dto.ReviewCreateResDTO;
import com.example.book_your_seat.review.controller.dto.ReviewResDTO;
import com.example.book_your_seat.review.service.facade.ReviewService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<ReviewCreateResDTO> saveReview(
            @RequestBody ReviewCreateReqDTO req,
            @LoginUser User loginUser) {

        ReviewCreateResDTO resDTO = reviewService.saveReview(loginUser.getId(), req.concertId(), req.content(), req.startCount());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resDTO);

    }

    @GetMapping("/{concertId}")
    public ResponseEntity< List<ReviewResDTO>> findAll(
            @RequestParam("concertId") Long concertId,
            @RequestParam("reviewId") Long reviewId,
            Pageable pageable){
        List<ReviewResDTO> reviewResDTOS = reviewService.pageNationReview(concertId, reviewId, pageable);

        return ResponseEntity.ok(reviewResDTOS);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReviewResDTO>> findUserReviewAll(
            @RequestParam("userId") Long userId,
            @RequestParam("reviewId") Long reviewId,
            Pageable pageable){
        List<ReviewResDTO> reviewResDTOS = reviewService.pageNationUserReview(reviewId, userId, pageable);

        return ResponseEntity.ok(reviewResDTOS);
    }



}
