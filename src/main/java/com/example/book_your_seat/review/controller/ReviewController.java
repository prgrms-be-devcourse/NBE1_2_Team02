package com.example.book_your_seat.review.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.review.controller.dto.ReviewCreateReqDTO;
import com.example.book_your_seat.review.controller.dto.ReviewCreateResDTO;
import com.example.book_your_seat.review.controller.dto.ReviewResDTO;
import com.example.book_your_seat.review.service.facade.ReviewService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
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
    public ResponseEntity< List<ReviewResDTO>> findAll(@PathVariable("concertId") Long concertId){
        List<ReviewResDTO> reviewResDTOS = reviewService.reviewAll(concertId);

        return ResponseEntity.ok(reviewResDTOS);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReviewResDTO>> findUserReviewAll(@PathVariable("userId") Long userId){
        List<ReviewResDTO> reviewResDTOS = reviewService.findUserReview(userId);

        return ResponseEntity.ok(reviewResDTOS);
    }



}
