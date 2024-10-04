package com.example.book_your_seat.service.seat;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.facade.SeatService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SeatCommandServiceImplTest extends IntegralTestSupport {
    @Autowired
    private ConcertCommandService concertCommandService;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatService seatService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        seatIds = seatRepository.findByConcertId(concertId)
                .stream()
                .map(Seat::getId)
                .collect(Collectors.toList());
    }

    @AfterEach
    void tearDown() {
        concertRepository.deleteAll();
        seatRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushAll();

    }

    @DisplayName("모든 남아있는 좌석을 선택하는 1000개의 요청이 들어 올 경우 99개의 요청은 실패한다")
    @Test
    void selectSeatTest() throws InterruptedException {
        // given
        SelectSeatRequest request = new SelectSeatRequest(1L,seatIds);

        // when
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    seatService.selectSeat(request);
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
        assertThat(successCount.get(), is(1));
        assertThat(failCount.get(), is(999));
    }

}