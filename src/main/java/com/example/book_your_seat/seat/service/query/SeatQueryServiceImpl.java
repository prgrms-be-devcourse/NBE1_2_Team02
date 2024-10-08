package com.example.book_your_seat.seat.service.query;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.book_your_seat.common.constants.Constants.INVALID_CONCERT;
import static com.example.book_your_seat.seat.SeatConst.SEAT_NOT_FOUND;

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

    @Override
    public List<Integer> findSeatNumbers(List<Long> seatIds) {
        return seatRepository.findValidSeats(seatIds).stream()
                .map(Seat::getSeatNumber)
                .toList();
    }

    @Override
    public Integer getSeatPrice(final Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(
                () -> new IllegalArgumentException(SEAT_NOT_FOUND)
        );

        return seat.getConcert().getPrice();
    }

    @Override
    public List<Seat> getSeats(List<Long> seatIds) {
        return seatRepository.findAllById(seatIds);
    }


    private void validateConcertDate(Concert concert) {
        LocalDate startDate = concert.getStartDate();
        LocalDate reservationDeadLine = startDate.minusDays(1);
        LocalDate now = LocalDate.now();
        if (now.isAfter(reservationDeadLine)) {
            throw new IllegalArgumentException(INVALID_CONCERT);
        }
    }

}
