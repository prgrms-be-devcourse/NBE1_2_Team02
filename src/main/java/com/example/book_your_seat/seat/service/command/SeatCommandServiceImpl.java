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
public class SeatCommandServiceImpl implements SeatCommandService {

    private final SeatRepository seatRepository;

    @Override
    public List<Seat> selectSeat(final SelectSeatRequest request) {
        List<Seat> seats = seatRepository.findAllByIdWithLock(request.seatIds());

        seats.forEach(seat -> {
            if (seat.isSold()) {
                log.info("seatId = {}", seat.getId());
                log.info("seat isSold = {}", seat.isSold());
                throw new IllegalArgumentException(SeatConst.SEAT_SOLD);
            }
            seat.selectSeat();
        });
        return seatRepository.saveAll(seats);
    }

    @Override
    public List<Seat> selectSeatWrite(SelectSeatRequest request) {
        List<Seat> seats = seatRepository.findAllByIdWithWrite(request.seatIds());

        seats.forEach(seat -> {
            if (seat.isSold()) {
                log.info("seatId = {}", seat.getId());
                log.info("seat isSold = {}", seat.isSold());
                throw new IllegalArgumentException(SeatConst.SEAT_SOLD);
            }
            seat.selectSeat();
        });
        return seatRepository.saveAll(seats);
    }

    @Override
    public int selectSeatRefactor(SelectSeatRequest request) {
        return seatRepository.findAllByIdWithRefactor(request.seatIds());
    }

    @Override
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
