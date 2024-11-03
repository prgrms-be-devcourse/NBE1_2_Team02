package com.example.book_your_seat.service.review;


import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.review.controller.dto.ReviewResDTO;
import com.example.book_your_seat.review.domain.Review;
import com.example.book_your_seat.review.repository.ReviewRepository;
import com.example.book_your_seat.review.service.facade.ReviewService;
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
    private ReviewService reviewService;

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
    public void makeReview() throws Exception {
       //given
        Long reviewId = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);
        //when
        
        Review review = reviewRepository.findById(reviewId).get();

        //then
        Assertions.assertThat(review.getContent()).isEqualTo("테스트1");
        Assertions.assertThat(review.getStarCount()).isEqualTo(5);
    }



    @Test
    @DisplayName("유저의 콘서트 리뷰 페이지네이션 ")
    public void reviewPageNationAll() throws Exception {
       //given
         reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);
         reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);
         reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

        PageRequest pageRequest = PageRequest.of(0, 5);//no-offset 이므로 임으로 페이지 number를 0으로 설정

        //when
        List<ReviewResDTO> reviewResDTOS = reviewService.pageNationReview(concert.getId(),null, pageRequest);//첫 페이지 조회

        //then
        Assertions.assertThat(reviewResDTOS.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("해당 리뷰유저는 리뷰를 수정할 수 있다.")
    public void reviewUpdate() throws Exception {
       //given
        Long saveId = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

        //when
        Long updateId = reviewService.updateReview(user.getId(), saveId, "테스트2", 4);


        Review review = reviewRepository.findById(updateId).get();
        //then
        Assertions.assertThat(review.getContent()).isEqualTo("테스트2");
        Assertions.assertThat(review.getStarCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("해당 리뷰유저는 리뷰를 삭제 할 수 있다.")
    public void reviewDelete() throws Exception {
       //given
        Long saveId = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

       //when
        reviewService.deleteReview(user.getId(), saveId);

        List<Review> reviewRepositoryAll = reviewRepository.findAll();

        //then
        Assertions.assertThat(reviewRepositoryAll.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("자신의 리뷰가 아닌 리뷰에 접근할 때 예외를 던진다.")
    public void accessReview() throws Exception {
        //given
        User user1= userRepository.save(new User("테스트", "테스트", "USER@email.com", "1234"));

        Long saveId = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);

        //when

        //then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            reviewService.updateReview(user1.getId(), saveId, "테스트2", 4);

        });
    }
}
