package com.example.book_your_seat.reservation.service.command;

import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;

import java.util.List;

public interface ReservationCommandService {
    Reservation createReservation(
            final User user, final Address address,
            final Payment payment,
            final Long userCouponId,
            final List<Seat> seats);


}
