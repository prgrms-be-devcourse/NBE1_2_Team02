package com.example.book_your_seat.seat.service.facade;

import static com.example.book_your_seat.queue.QueueConst.NOT_IN_PROCESSING_QUEUE;

import com.example.book_your_seat.aop.seatLock.SeatLock;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import com.example.book_your_seat.seat.redis.SeatRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatFacade {

    private final SeatCommandService seatCommandService;
    private final SeatQueryService seatQueryService;
    private final SeatRedisService redisService;
    private final QueueRedisRepository queueRedisRepository;

    public List<SeatResponse> findAllSeats(Long concertId) {
        return seatQueryService.findAllSeats(concertId).stream()
                .map(SeatResponse::from)
                .toList();
    }

    public SelectSeatResponse selectSeat(final SelectSeatRequest request, final Long userId) {
        checkInProcessingQueue(request.concertId(), request.queueToken());

        List<Seat> seats = seatCommandService.selectSeat(request);

        redisService.cacheSeatIds(seats, userId);

        return SelectSeatResponse.fromSeats(seats);
    }

    @SeatLock
    public SelectSeatResponse selectSeatRedisson(final SelectSeatRequest request, final Long userId) {
        checkInProcessingQueue(request.concertId(), request.queueToken());
        List<Seat> seats = seatCommandService.selectSeatRedisson(request);
        redisService.cacheSeatIds(seats, userId);

        return SelectSeatResponse.fromSeats(seats);
    }

    private void checkInProcessingQueue(Long concertId, String queueToken) {
        if (!queueRedisRepository.isInProcessingQueue(concertId, queueToken)) {
            throw new IllegalArgumentException(NOT_IN_PROCESSING_QUEUE);
        }
    }


    public List<Seat> getSeats(final List<Long> seatIds) {
        return seatQueryService.getSeats(seatIds);
    }

    public void seatReservationComplete(final List<Seat> seats, final Reservation reservation) {
        seatCommandService.seatReservationComplete(seats, reservation);
    }

}
