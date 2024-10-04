package com.example.book_your_seat.seat.service.facade;

import com.example.book_your_seat.aop.seatLock.SeatLock;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import com.example.book_your_seat.seat.service.redis.SeatRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatCommandService seatCommandService;
    private final SeatQueryService seatQueryService;
    private final SeatRedisService redisService;

    public List<Seat> findAllSeats(Long concertId) {
        return seatQueryService.findAllSeats(concertId);
    }

    @Override
    public SelectSeatResponse selectSeat(final SelectSeatRequest request) {
        List<Seat> seats = seatCommandService.selectSeat(request);

        redisService.cacheSeatIds(seats, request.userId());

        return SelectSeatResponse.fromSeats(seats);
    }

    @Override
    @SeatLock
    public SelectSeatResponse selectSeatRedisson(final SelectSeatRequest request) {
        List<Seat> seats = seatCommandService.selectSeatRedisson(request);

        redisService.cacheSeatIds(seats, request.userId());

        return SelectSeatResponse.fromSeats(seats);
    }

    @Override
    public Integer getSeatPrice(final Long seatId) {
        return seatQueryService.getSeatPrice(seatId);
    }


    @Override
    public List<Seat> getSeats(final List<Long> seatIds) {
        return seatQueryService.getSeats(seatIds);
    }

    @Override
    public void seatReservationComplete(final List<Seat> seats, final Reservation reservation) {
        seatCommandService.seatReservationComplete(seats, reservation);
    }


}
