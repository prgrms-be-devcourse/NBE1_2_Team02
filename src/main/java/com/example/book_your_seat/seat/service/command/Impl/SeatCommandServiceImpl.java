package com.example.book_your_seat.seat.service.command.Impl;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.SeatConst;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Primary
@Qualifier("Pessimistic")
public class SeatCommandServiceImpl implements SeatCommandService {

    private final SeatRepository seatRepository;

    public List<Seat> selectSeat(final SelectSeatRequest request) {
        List<Seat> seats = seatRepository.findAllByIdWithLock(request.seatIds());

        seats.forEach(seat -> {
            if (seat.isSold()) {
                throw new IllegalArgumentException(SeatConst.SEAT_SOLD);
            }
            seat.selectSeat();
        });
        return seatRepository.saveAll(seats);
    }

    public List<Seat> selectSeatRedisson(final SelectSeatRequest request) {
        List<Seat> seats = seatRepository.findAllById(request.seatIds());

        seats.forEach(seat -> {
            if (seat.isSold()) {
                throw new IllegalArgumentException(SeatConst.SEAT_SOLD);
            }
            seat.selectSeat();
        });
        return seatRepository.saveAll(seats);
    }

    @Override
    public void seatReservationComplete(final List<Seat> seats, final Reservation reservation) {
        seats.forEach(seat -> {
            seat.assignReservation(reservation);
        });

        seatRepository.saveAll(seats);
    }
}
