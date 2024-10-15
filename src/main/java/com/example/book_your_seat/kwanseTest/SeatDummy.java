package com.example.book_your_seat.kwanseTest;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.seat.repository.SeatRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class SeatDummy {

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void insertDummyData() {
        Concert concert = new Concert(
                "request.title",
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                10000,
                14
        );
        concertRepository.saveBulkData(concert);
    }

    @PreDestroy
    @Transactional
    public void removeDummyData() {
        seatRepository.deleteAll();
        concertRepository.deleteAll();
    }
}
