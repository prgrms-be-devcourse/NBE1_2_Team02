package com.example.book_your_seat.reservation.service.facade;


import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.command.ReservationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ReservationFacade {

    private final ReservationCommandService reservationCommandService;


    public Reservation save(Reservation reservation) {
        return reservationCommandService.saveReservation(reservation);
    }


}
