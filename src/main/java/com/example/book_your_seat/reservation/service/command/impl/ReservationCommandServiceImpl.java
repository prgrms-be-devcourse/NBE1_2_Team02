package com.example.book_your_seat.reservation.service.command.impl;

import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.reservation.service.command.ReservationCommandService;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class ReservationCommandServiceImpl implements ReservationCommandService {
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(
            final User user,
            final Address address,
            final Payment payment,
            final Long userCouponId,
            final List<Seat> seats) {
        return reservationRepository.save(
                new Reservation(
                        ReservationStatus.ORDERED,
                        userCouponId,
                        payment.getTotalPrice(),
                        user,
                        address,
                        payment
                )
        );
    }
}
