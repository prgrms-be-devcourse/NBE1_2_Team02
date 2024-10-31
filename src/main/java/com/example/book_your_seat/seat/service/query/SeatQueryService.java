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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatQueryService {

    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;

    public List<Seat> findAllSeats(Long concertId) {

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT));

        validateConcertDate(concert);
        return seatRepository.findByConcertId(concertId);
    }

    public List<Integer> findSeatNumbers(List<Long> seatIds) {
        return seatRepository.findValidSeats(seatIds).stream()
                .map(Seat::getSeatNumber)
                .toList();
    }

    public Integer getSeatPrice(final List<Long> seatsId) {
        List<Seat> seats = seatRepository.findAllById(seatsId);
        int concertPrice = seats.get(0).getConcert().getPrice();

        return seats.stream()
                .map(Seat::getZone)
                .mapToInt(zone -> zone.applyZonePrice(concertPrice))
                .sum();
    }

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
