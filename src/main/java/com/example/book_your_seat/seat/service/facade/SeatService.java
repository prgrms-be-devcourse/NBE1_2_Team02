package com.example.book_your_seat.seat.service.facade;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.controller.dto.RemainSeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public interface SeatService {
    SelectSeatResponse selectSeat(final SelectSeatRequest request);

    SelectSeatResponse selectSeatRedisson(final SelectSeatRequest request);

    List<RemainSeatResponse> findRemainSeats(final Long concertId);

    Integer getSeatPrice(final Long seatId);

    List<Seat> getSeats(final List<Long> seatIds);

    void seatReservationComplete(final List<Seat> seats, final Reservation reservation);
}
