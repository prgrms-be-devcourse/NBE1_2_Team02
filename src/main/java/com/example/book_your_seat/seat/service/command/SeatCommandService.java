package com.example.book_your_seat.seat.service.command;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.SeatConst;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Primary
@Slf4j
public class SeatCommandService {

    private final SeatRepository seatRepository;

    public List<Seat> selectSeat(final Long concertId,
                                 final SelectSeatRequest request
    ) {
        List<Seat> seats = seatRepository.findAllByIdWithLock(concertId, request.seatNumbers());
        seats.forEach(seat -> {
            if (seat.isSold()) {
                throw new IllegalArgumentException(SeatConst.SEAT_SOLD);
            }
            seat.selectSeat();
        });

        return seatRepository.saveAll(seats);
    }

}
