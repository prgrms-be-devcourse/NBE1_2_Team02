package com.example.book_your_seat.seat.service.redis;

import com.example.book_your_seat.reservation.contorller.dto.PaymentRequest;
import com.example.book_your_seat.seat.domain.Seat;

import java.util.List;

public interface SeatRedisService {
    void cacheSeatIds(List<Seat> seats, Long userId);

    void validateSeat(PaymentRequest request, Long userId);

    void deleteCache(final Long seatId);
}
