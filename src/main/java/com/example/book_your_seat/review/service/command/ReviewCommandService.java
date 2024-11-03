package com.example.book_your_seat.review.service.command;


import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.review.controller.dto.ReviewCreateResDTO;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.review.repository.ReviewRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.concert.ConcertConst.INVALID_CONCERT_ID;
import static com.example.book_your_seat.review.ReviewConst.NOT_FOUND_REVIEW;
import static com.example.book_your_seat.review.ReviewConst.NOT_UPDATE_ACCESS;
import static com.example.book_your_seat.user.UserConst.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCommandService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final ConcertRepository concertRepository;


    public Long createReview(Long userId, Long concertId, String content, int startCount){

        User user = getUser(userId);
        Concert concert = getConcert(concertId);

        Review review = Review.from(content, startCount, user, concert);

        Review saveReview = reviewRepository.save(review);

        return saveReview.getId();
    }

    public Long updateReview(Long userId, Long reviewId, String content, int startCount){
        validationReview(userId);

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_REVIEW));

        review.updateReview(content, startCount);

        return reviewId;

    }


    public Long deleteReview(Long userId, Long reviewId){

        validationReview(userId);

        reviewRepository.deleteById(reviewId);

        return reviewId;
    }

    private void validationReview(Long userId) {
        if(!reviewRepository.existsByUserId(userId)){
            throw new IllegalArgumentException(NOT_UPDATE_ACCESS);
        }
    }

    private Concert getConcert(Long concertId) {
       return concertRepository.findById(concertId).orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT_ID + concertId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    }


}
