package com.example.book_your_seat.seat.service.command;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.dto.SelectSeatsCommand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeatCommandService {

    List<Seat> selectSeat(final SelectSeatRequest request);

    List<Seat> selectSeatRedisson(final SelectSeatRequest request);

    void seatReservationComplete(final List<Seat> seats, final Reservation reservation);

}
