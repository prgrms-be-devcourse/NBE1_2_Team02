package com.example.book_your_seat.seat.service.redis;

import com.example.book_your_seat.reservation.contorller.dto.ConfirmationReservationRequest;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public interface SeatRedisService {
    void cacheSeatIds(final List<Seat> seats, final Long userId);

    void validateSeat(final ConfirmationReservationRequest request);

    void deleteCache(final Long seatId);
}
