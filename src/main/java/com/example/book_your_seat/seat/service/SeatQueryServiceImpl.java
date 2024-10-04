package com.example.book_your_seat.seat.service;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.book_your_seat.reservation.ReservationConst.INVALID_CONCERT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatQueryServiceImpl implements SeatQueryService {

    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;


    @Override
    public List<Seat> findAllSeats(Long concertId) {

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT));

        validateConcertDate(concert);

        return seatRepository.findByConcertId(concertId);
    }

    private static void validateConcertDate(Concert concert) {
        LocalDate startDate = concert.getStartDate();
        LocalDate reservationDeadLine = startDate.minusDays(1);
        LocalDate now = LocalDate.now();
        if (now.isAfter(reservationDeadLine)) {
            throw new IllegalArgumentException(INVALID_CONCERT);
        }
    }
}
