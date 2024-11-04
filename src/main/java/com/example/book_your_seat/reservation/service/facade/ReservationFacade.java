package com.example.book_your_seat.reservation.service.facade;


import com.example.book_your_seat.reservation.contorller.dto.request.CancelReservationRequest;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.command.ReservationCommandService;
import com.example.book_your_seat.reservation.service.query.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationFacade {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    public Reservation save(Reservation reservation) {
        return reservationCommandService.saveReservation(reservation);
    }

    public String getValidPaymentKey(final CancelReservationRequest request){
        return reservationQueryService.validateReservation(request);
    }

    public void cancelReservation(final CancelReservationRequest request){
        reservationCommandService.cancelReservation(request);
    }
}
