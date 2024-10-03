package com.example.book_your_seat.reservation.service;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.book_your_seat.reservation.ReservationConst.INVALID_RESERVATION;

@Service
@RequiredArgsConstructor
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;

    @Override
    public Reservation findReservationById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_RESERVATION));
    }

    @Override
    public Reservation findReservationBySeatsId(List<Long> seatsId) {
        return reservationRepository.findReservationBySeatsId(seatsId);
    }
}
