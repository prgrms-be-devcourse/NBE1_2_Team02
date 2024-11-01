package com.example.book_your_seat.reservation.service.query;

import com.example.book_your_seat.reservation.contorller.dto.request.CancelReservationRequest;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.reservation.ReservationConst.RESERVATION_CAN_NOT_CANCEL;
import static com.example.book_your_seat.reservation.domain.ReservationStatus.ORDERED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryService {
    private final ReservationRepository reservationRepository;

    public Reservation validateReservation(final CancelReservationRequest request) {
        Reservation reservation = reservationRepository.findReservationWithPayment(request.ReservationId());
        if (!canCancelReservation(reservation)) {
            throw new IllegalArgumentException(RESERVATION_CAN_NOT_CANCEL);
        }
        return reservation;
    }

    private boolean canCancelReservation(final Reservation reservation) {
        return reservation.getStatus() == ORDERED;
    }
}
