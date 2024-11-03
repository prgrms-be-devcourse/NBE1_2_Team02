package com.example.book_your_seat.service.review;


import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.review.controller.dto.ReviewCreateResDTO;
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

public class ReviewTest extends IntegralTestSupport {

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
        ReviewCreateResDTO resDTO = reviewService.saveReview(user.getId(), concert.getId(), "테스트1", 5);
        //when
        
        Review review = reviewRepository.findById(resDTO.id()).get();

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
        List<ReviewResDTO> reviewResDTOS = reviewService.pageNationReview(null, concert.getId(), pageRequest);//첫 페이지 조회

        //then
        Assertions.assertThat(reviewResDTOS.size()).isEqualTo(5);
    }
}
