package com.example.book_your_seat.seat.service.facade;

import com.example.book_your_seat.aop.seatLock.SeatLock;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import com.example.book_your_seat.seat.controller.dto.SeatResponse;
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.redis.SeatRedisService;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import com.example.book_your_seat.seat.service.query.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.book_your_seat.queue.QueueConst.NOT_IN_PROCESSING_QUEUE;

@Service
@RequiredArgsConstructor
public class SeatFacade {

    private final SeatCommandService seatCommandService;
    private final SeatQueryService seatQueryService;
    private final SeatRedisService redisService;
    private final QueueRedisRepository queueRedisRepository;

    public SeatResponse findAllSeats(Long concertId) {
        List<Seat> allSeats = seatQueryService.findAllSeats(concertId);
        return SeatResponse.from(allSeats);
    }

    public SelectSeatResponse selectSeat(final SelectSeatRequest request, final Long userId) {
        checkInProcessingQueue(request.concertId(), request.queueToken());

        List<Seat> seats = seatCommandService.selectSeat(concertId, request);
        redisService.cacheSeatIds(seats, userId);
        return SeatResponse.from(seats);
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


}
