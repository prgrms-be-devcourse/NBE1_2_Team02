package com.example.book_your_seat.service.like;

import com.example.DbCleaner;
import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.likeconcert.service.facade.LikeConcertFacade;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

class LikeConcertServiceTest extends IntegralTestSupport {

    @Autowired
    DbCleaner dbCleaner;

    @Autowired
    private LikeConcertFacade likeConcertFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @BeforeEach
    void setUp() {
        dbCleaner.cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        dbCleaner.cleanDatabase();
    }

    @DisplayName("관심 공연 등록을 하면 등록한 관심공연들을 목록으로 조회할 수 있다")
    @Test
    void likeTest() {

        // given

        User user = userRepository.save(new User(
                "khan_nickname",
                "khan_username",
                "khan_email",
                "password"));

        Concert concert = concertRepository.save(new Concert(
                "title",
                LocalDate.now().plusDays(30),
                LocalDate.now().plusDays(60),
                10000,
                2
        ));


        // when
        likeConcertFacade.addLike(user.getId(), concert.getId());
        List<ConcertListResponse> results = likeConcertFacade.findLikesByUserId(user.getId());

        // then
        Assertions.assertThat(results)
                .extracting(ConcertListResponse::getTitle)
                .contains("title");
    }
}
