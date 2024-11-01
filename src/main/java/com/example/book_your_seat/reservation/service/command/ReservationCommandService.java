package com.example.book_your_seat.reservation.service.command;

import com.example.book_your_seat.reservation.contorller.dto.request.CancelReservationRequest;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationCommandService {

    private final ReservationRepository reservationRepository;

    public Reservation saveReservation(final Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(final CancelReservationRequest request) {
        Reservation reservation = reservationRepository.findReservationWithPaymentAndSeats(request.ReservationId());

        reservation.cancelReservation();

        reservation.getPayment().cancelPayment();

        reservation.getSeats().forEach(Seat::releaseSeat);

        reservationRepository.save(reservation);
    }

}
