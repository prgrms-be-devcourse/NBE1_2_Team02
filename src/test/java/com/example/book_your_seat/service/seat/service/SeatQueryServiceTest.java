package com.example.book_your_seat.service.seat.service;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SeatQueryServiceTest extends IntegerTestSupport {

    @Autowired
    private SeatQueryService seatQueryService;

    @Autowired
    private ConcertCommandService concertCommandService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatCommandService seatCommandService;
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
        List<RemainSeatResponse> remainSeats = seatQueryService.findRemainSeats(concertId);

        //then
        assertThat(remainSeats.isEmpty(), is(false));

    }

    @DisplayName("모든 남아있는 좌석을 선택하는 100개의 요청이 들어 올 경우 99개의 요청은 실패한다")
    @Test
    void selectSeatTest() throws InterruptedException {
        // given
        SelectSeatRequest request = new SelectSeatRequest(seatIds);

        // when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    seatCommandService.selectSeat(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        List<RemainSeatResponse> remainSeats = seatQueryService.findRemainSeats(concertId);
        assertThat(remainSeats.isEmpty(), is(true)); // 잔여좌석 0개
        assertThat(successCount.get(), is(1));
        assertThat(failCount.get(), is(99));
    }
}