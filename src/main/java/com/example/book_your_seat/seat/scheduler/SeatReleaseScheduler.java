package com.example.book_your_seat.seat.scheduler;

import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatReleaseScheduler {

    private final SeatRepository seatRepository;

    @Scheduled(fixedRate = 60000) // 매 분마다 실행
    @Transactional
    public void releaseUnreservedSeats() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<Seat> seatsToRelease = seatRepository.findSeatsToRelease(thirtyMinutesAgo);

        seatsToRelease.forEach(Seat::releaseSeat);
        seatRepository.saveAll(seatsToRelease);
    }
}
