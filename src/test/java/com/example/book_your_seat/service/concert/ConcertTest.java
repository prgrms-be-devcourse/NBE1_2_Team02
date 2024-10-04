package com.example.book_your_seat.service.concert;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.domain.Concert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

class ConcertTest extends IntegralTestSupport {

    // 예매 시작 시간 검증필요..

    @DisplayName("공연 등록 시 예매 가능 시간은 공연 시작일로부터 일주일 전 낮 12시로 설정된다.")
    @Test
    void reservationTimeTest() {
        // given, when
        Concert concert = new Concert(
                "title",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024,9,25),
                10000L,
                120
        );

        // then
        Assertions.assertThat(concert.getReservationStartAt())
                .isEqualTo(LocalDateTime.of(2024, 9, 17, 12, 0, 0));
    }
}
