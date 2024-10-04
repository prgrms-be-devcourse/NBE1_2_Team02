package com.example.book_your_seat.service.seat;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.facade.SeatService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SeatQueryServiceTest extends IntegerTestSupport {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ConcertCommandService concertCommandService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private SeatRepository seatRepository;

    private Long concertId;
    private List<Long> seatIds;

    @BeforeEach
    void setUp() {
        AddConcertRequest request = new AddConcertRequest(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024, 9, 25),
                10000,
                120
        );

        concertId = concertCommandService.add(request);
        seatIds = seatRepository.findByConcertIdAndNotSold(concertId)
                .stream()
                .map(Seat::getId)
                .collect(Collectors.toList());
    }

    @AfterEach
    void tearDown() {
        concertRepository.deleteAll();
        seatRepository.deleteAll();
    }

    @DisplayName("concertId를 입력 하면 seat중에 예약이 안된 시트를 반환한다.")
    @Test
    void remainSeatTest() {
        // given
        //when
        List<RemainSeatResponse> remainSeats = seatService.findRemainSeats(concertId);

        //then
        assertThat(remainSeats.isEmpty(), is(false));

    }
}