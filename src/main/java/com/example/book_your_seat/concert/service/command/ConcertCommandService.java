package com.example.book_your_seat.concert.service.command;


import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.domain.SeatId;
import com.example.book_your_seat.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static com.example.book_your_seat.concert.ConcertConst.INVALID_CONCERT_ID;
import static com.example.book_your_seat.concert.ConcertConst.TOTAL_STOCK;

@Service
@Transactional
@RequiredArgsConstructor
public class ConcertCommandService {

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    public Long add(final AddConcertRequest request) {

        Concert concert = AddConcertRequest.to(request);
        concertRepository.save(concert);

        List<Seat> seats = IntStream.rangeClosed(1, TOTAL_STOCK)
                .mapToObj(i -> new SeatId(concert.getId(), (long) i))
                .map(Seat::new)
                .toList();
        seatRepository.saveAll(seats);

        return concert.getId();
    }

    public void delete(final Long id) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT_ID + id));
        concertRepository.delete(concert);
        seatRepository.deleteById_ConcertId(concert.getId());
    }
}
