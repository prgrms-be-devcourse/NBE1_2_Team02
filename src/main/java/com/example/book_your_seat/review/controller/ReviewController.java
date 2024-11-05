package com.example.book_your_seat.review.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.review.controller.dto.ReviewCreateRequest;
import com.example.book_your_seat.review.controller.dto.ReviewListResponse;
import com.example.book_your_seat.review.controller.dto.ReviewResponse;
import com.example.book_your_seat.review.controller.dto.ReviewUpdateDTO;
import com.example.book_your_seat.review.service.facade.ReviewFacade;
import com.example.book_your_seat.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewFacade reviewService;

    @Operation(
            summary = "리뷰를 저장합니다.",
            description = "리뷰를 저장합니다."
    )
    @PostMapping
    public ResponseEntity<ReviewResponse> saveReview(
            @RequestBody ReviewCreateRequest req,
            @LoginUser User loginUser) {

        ReviewResponse reviewResponse = reviewService.saveReview(
                loginUser.getId(),
                req.concertId(),
                req.content(),
                req.startCount()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewResponse);
    }


    @Operation(
            summary = "모든 리뷰를 조회합니다.",
            description = "모든 리뷰를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<ReviewListResponse>> findAll(
            @RequestParam("concertId") Long concertId,
            @RequestParam(value = "reviewId", required = false) Long reviewId,
            Pageable pageable) {

        log.info("concertId: {}, reviewId: {}", concertId, reviewId);

        List<ReviewListResponse> reviewResDTOS = reviewService.pageNationReview(
                concertId,
                reviewId,
                pageable
        );

        return ResponseEntity.ok(reviewResDTOS);
    }

    @Operation(
            summary = "특정 유저의 모든 리뷰를 조회합니다.",
            description = "특정 유저의 모든 리뷰를 조회합니다."
    )
    @GetMapping("/userConcert")
    public ResponseEntity<List<ReviewListResponse>> findUserReviewAll(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "reviewId", required = false) Long reviewId,
            Pageable pageable) {

        List<ReviewListResponse> reviewResDTOS = reviewService.pageNationUserReview(
                userId,
                reviewId,
                pageable
        );

        return ResponseEntity.ok(reviewResDTOS);
    }

    @Operation(
            summary = "리뷰를 수정합니다.",
            description = "리뷰를 수정합니다."
    )
    @PatchMapping
    public ResponseEntity<ReviewResponse> updateReview(
            @RequestBody ReviewUpdateDTO reviewUpdateDTO,
            @LoginUser User user) {

        ReviewResponse reviewResponse = reviewService.updateReview(
                user.getId(),
                reviewUpdateDTO.reviewId(),
                reviewUpdateDTO.content(),
                reviewUpdateDTO.startCount()
        );

        return ResponseEntity.ok(reviewResponse);
    }

    @Operation(
            summary = "리뷰를 삭제합니다.",
            description = "리뷰를 삭제합니다."
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @LoginUser User user,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(user.getId(), reviewId);
        return ResponseEntity.ok().build();
    }
}
