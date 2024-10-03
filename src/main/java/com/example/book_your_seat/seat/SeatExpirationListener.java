package com.example.book_your_seat.seat;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.ReservationQueryService;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.service.facade.SeatCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SeatExpirationListener implements MessageListener {
    
    private final ReservationQueryService reservationQueryService;
    private final SeatCommandService seatCommandService;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        Reservation reservation = getReservation(message);
        if (reservation.isNotOrdered()) {
            cancelSeats(reservation);
        }
    }

    private Reservation getReservation(Message message) {
        String expiredKey = message.toString();
        UUID reservationId = UUID.fromString(expiredKey);
        return reservationQueryService.findReservationById(reservationId);
    }


    private void cancelSeats(Reservation reservation) {
        List<Long> seatsId = reservation.getSeats().stream()
                .map(Seat::getId)
                .toList();
        
        seatCommandService.cancelSeats(seatsId);
    }
}
