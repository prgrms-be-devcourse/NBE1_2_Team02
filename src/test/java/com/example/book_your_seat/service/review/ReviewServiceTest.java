package com.example.book_your_seat.service.review;


import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.review.controller.dto.ReviewListResponse;
import com.example.book_your_seat.review.controller.dto.ReviewResponse;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.review.repository.ReviewRepository;
import com.example.book_your_seat.review.service.facade.ReviewFacade;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewServiceTest extends IntegralTestSupport {

    @Autowired
    private ReviewFacade reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Concert concert;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User("테스트", "테스트", "USER@email.com", "1234"));
        concert = concertRepository.save(new Concert("테스트 콘서트", LocalDate.now(), LocalDate.now(), 10000, 10));

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(Review.from("테스트1", 5, user, concert));
        reviewList.add(Review.from("테스트2", 4, user, concert));

        reviewRepository.saveAll(reviewList);
    }

    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
        concertRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저가 해당 콘서트에 대한 리뷰를 쓴다")
    public void makeReview() {
        // given
        ReviewResponse reviewResponse = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

        // when
        Review review = reviewRepository.findById(reviewResponse.id()).orElseThrow();

        // then
        Assertions.assertThat(review.getContent()).isEqualTo("테스트1");
        Assertions.assertThat(review.getStarCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("유저의 콘서트 리뷰 페이지네이션")
    public void reviewPageNationAll() {
        // given
        for (int i = 0; i < 3; i++) {
            reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);
        }
        PageRequest pageRequest = PageRequest.of(0, 5);

        // when
        List<ReviewListResponse> reviewResDTOS = reviewService.pageNationReview(concert.getId(), null, pageRequest);

        // then
        Assertions.assertThat(reviewResDTOS.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("해당 리뷰 유저는 리뷰를 수정할 수 있다.")
    public void reviewUpdate() {
        // given
        ReviewResponse reviewResponse = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

        // when
        ReviewResponse updateReview = reviewService.updateReview(user.getId(), reviewResponse.id(), "테스트2", 4);
        Review review = reviewRepository.findById(updateReview.id()).orElseThrow();

        // then
        Assertions.assertThat(review.getContent()).isEqualTo("테스트2");
        Assertions.assertThat(review.getStarCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("해당 리뷰 유저는 리뷰를 삭제할 수 있다.")
    public void reviewDelete() {
        // given
        ReviewResponse reviewResponse = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

        // when
        reviewService.deleteReview(user.getId(), reviewResponse.id());
        List<Review> reviewRepositoryAll = reviewRepository.findAll();

        // then
        Assertions.assertThat(reviewRepositoryAll.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("자신의 리뷰가 아닌 리뷰에 접근할 때 예외를 던진다.")
    public void accessReview() {
        // given
        User user1 = userRepository.save(new User("테스트", "테스트", "USER@email.com", "1234"));
        ReviewResponse reviewResponse = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            reviewService.updateReview(user1.getId(), reviewResponse.id(), "테스트2", 4);
        });
    }
}

