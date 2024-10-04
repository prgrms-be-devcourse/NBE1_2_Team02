package com.example.book_your_seat.service.concert;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.concert.service.ConcertQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class ConcertServiceTest extends IntegralTestSupport {

    @Autowired
    private ConcertCommandService concertCommandService;

    @Autowired
    private ConcertQueryService concertQueryService;


    @DisplayName("제목, 시작 시간, 종료 시간, 금액, 러닝 타임을 입력하면 Concert 를 생성한다.")
    @Test
    void addTest() {
        // given
        AddConcertRequest request = new AddConcertRequest(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024,9,25),
                10000L,
                120
        );

        // when
        Long id = concertCommandService.add(request);
        ConcertResponse response = concertQueryService.findById(id);

        // then  쿼리 101개 전송.. Concert 1, Seat 100..
        Assertions.assertThat(response)
                .extracting("title", "startDate", "endDate", "price", "startHour")
                        .containsExactly(
                                "제목1",
                                LocalDate.of(2024, 9, 24),
                                LocalDate.of(2024, 9, 25),
                                10000L,
                                120
                        );
    }

    @DisplayName("Concert 를 등록하면 예매 가능 날짜는 공연 시작일로부터 일주일 전, 낮 12시로 설정된다.")
    @Test
    void reservationTimeTest() {
        // given
        AddConcertRequest request = new AddConcertRequest(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024,9,25),
                10000L,
                14
        );
        Long id = concertCommandService.add(request);

        // when
        ConcertResponse response = concertQueryService.findById(id);

        Assertions.assertThat(response.getReservationStartAt())
                .isEqualTo(LocalDateTime.of(2024, 9, 17, 12, 0, 0));
    }

    @DisplayName("Concert 전체 목록을 조회한다.")
    @Test
    void findAllTest() {
        // given
        AddConcertRequest request1 = new AddConcertRequest(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024,9,25),
                10000L,
                120
        );
        AddConcertRequest request2 = new AddConcertRequest(
                "제목2",
                LocalDate.of(2024, 10, 24),
                LocalDate.of(2024,11,25),
                90000L,
                999
        );
        concertCommandService.add(request1);
        concertCommandService.add(request2);

        // when
        List<ConcertResponse> responses = concertQueryService.findAll();

        // then
        Assertions.assertThat(responses)
                .extracting(ConcertResponse::getTitle)
                .contains("제목1", "제목2");
    }

    @DisplayName("Concert 의 Id 를 전송하면 해당 Concert 의 정보를 조회한다")
    @Test
    void findByIdTest() {
        // given
        AddConcertRequest request = new AddConcertRequest(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024,9,25),
                10000L,
                120
        );
        Long id = concertCommandService.add(request);

        // when
        ConcertResponse result = concertQueryService.findById(id);

        // then
        Assertions.assertThat(result)
                .extracting("title", "startDate", "endDate", "price", "startHour")
                .containsExactly(
                        "제목1",
                        LocalDate.of(2024, 9, 24),
                        LocalDate.of(2024, 9, 25),
                        10000L,
                        120
                );
    }



    @DisplayName("Concert 의 Id 를 입력받아서 해당 Concert 를 삭제한다.")
    @Test
    void deleteTest() {
        // given
        AddConcertRequest request = new AddConcertRequest(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024,9,25),
                10000L,
                120
        );
        Long id = concertCommandService.add(request);

        // when
        concertCommandService.delete(id);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> concertQueryService.findById(id));
    }

}
