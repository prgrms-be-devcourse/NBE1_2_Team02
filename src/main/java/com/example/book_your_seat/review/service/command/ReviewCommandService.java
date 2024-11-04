package com.example.book_your_seat.review.service.command;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.service.query.ConcertQueryService;
import com.example.book_your_seat.review.controller.dto.ReviewResponse;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.review.repository.ReviewRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.service.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.review.ReviewConst.NOT_FOUND_REVIEW;
import static com.example.book_your_seat.review.ReviewConst.NOT_UPDATE_ACCESS;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final UserQueryService userQueryService;
    private final ConcertQueryService concertQueryService;

    /**
     * 새로운 리뷰를 생성하고 저장합니다.
     */
    public ReviewResponse createReview(Long userId, Long concertId, String content, int startCount) {
        User user = userQueryService.getUserByUserId(userId);
        Concert concert = concertQueryService.findByConcertId(concertId);

        Review review = Review.from(content, startCount, user, concert);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }

    /**
     * 특정 리뷰를 업데이트합니다.
     */
    public ReviewResponse updateReview(Long userId, Long reviewId, String content, int starCount) {
        validateUserReviewAccess(userId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_REVIEW));

        review.updateReview(content, starCount);
        return ReviewResponse.from(review);
    }

    /**
     * 특정 리뷰를 삭제합니다.
     */
    public void deleteReview(Long userId, Long reviewId) {
        validateUserReviewAccess(userId);
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 사용자가 해당 리뷰에 접근 권한이 있는지 검증합니다.
     */
    private void validateUserReviewAccess(Long userId) {
        if (!reviewRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(NOT_UPDATE_ACCESS);
        }
    }
}




}
