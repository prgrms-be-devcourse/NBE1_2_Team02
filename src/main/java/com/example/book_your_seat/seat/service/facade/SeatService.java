package com.example.book_your_seat.seat.service.facade;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;


public interface SeatService {
    List<SeatResponse> findAllSeats(Long concertId);

    SelectSeatResponse selectSeat(final SelectSeatRequest request);

    SelectSeatResponse selectSeatRedisson(final SelectSeatRequest request);

    Integer getSeatPrice(final Long seatId);

    List<Seat> getSeats(final List<Long> seatIds);

    void seatReservationComplete(final List<Seat> seats, final Reservation reservation);

}
