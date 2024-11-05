package com.example.book_your_seat.seat.service.query;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.payment.controller.dto.request.FinalPriceRequest;
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

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    public List<Seat> findAllSeats(Long concertId) {
        return seatRepository.findByConcertId(concertId);
    }

    public Integer getSeatPrice(final FinalPriceRequest request) {
        List<Seat> seats = seatRepository.findAllBySeatId(request.concertId(), request.seatNumbers());
        int concertPrice = concertRepository.findById(request.concertId())
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT))
                .getPrice();

        return seats.stream()
                .map(Seat::getZone)
                .mapToInt(zone -> zone.applyZonePrice(concertPrice))
                .sum();
    }

}
