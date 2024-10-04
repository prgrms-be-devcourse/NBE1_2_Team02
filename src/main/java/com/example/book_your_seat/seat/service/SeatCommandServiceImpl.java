package com.example.book_your_seat.seat.service;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.dto.SelectSeatsCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.book_your_seat.common.constants.Constants.EXISTS_INVALID_SEAT;

@RequiredArgsConstructor
@Service
@Transactional
public class SeatCommandServiceImpl implements SeatCommandService {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    private final RedisTemplate<UUID, List<Long>> redisTemplate;

    @Override
    public void selectSeats(SelectSeatsCommand command) {

        List<Long> seatsId = command.getSeatsId();
        Long affectedRows = seatRepository.reserveSeats(command.getConcertId(), command.getSeatsId());
        if (seatsId.size() != affectedRows) {
            throw new IllegalArgumentException(EXISTS_INVALID_SEAT);
        }
        setTimeLimit(command);
    }

    private void setTimeLimit(SelectSeatsCommand command) {

        UUID reservationId = UUID.randomUUID();
        List<Seat> seats = seatRepository.findAllById(command.getSeatsId());

        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .userId(command.getUserId())
                .addressId(command.getAddressId())
                .seats(seats)
                .build();
        reservationRepository.save(reservation);
        redisTemplate.opsForValue().set(reservationId, command.getSeatsId(),30, TimeUnit.MINUTES);
    }

    @Override
    public void cancelSeats(List<Long> seatsId) {
        seatRepository.cancelSeats(seatsId);
    }
}
