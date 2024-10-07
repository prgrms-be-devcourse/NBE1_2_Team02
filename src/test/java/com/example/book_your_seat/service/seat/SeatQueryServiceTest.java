package com.example.book_your_seat.service.seat;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.facade.SeatFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

class SeatQueryServiceTest extends IntegralTestSupport {

    @Autowired
    private SeatFacade seatFacade;

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
        seatIds = seatRepository.findByConcertId(concertId)
                .stream()
                .map(Seat::getId)
                .collect(Collectors.toList());
    }

    @AfterEach
    void tearDown() {
        concertRepository.deleteAll();
        seatRepository.deleteAll();
    }
}