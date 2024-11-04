package com.example.book_your_seat.reservation.service.command;

import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.repository.PaymentRepository;
import com.example.book_your_seat.reservation.contorller.dto.request.CancelReservationRequest;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.book_your_seat.payment.PaymentConst.INVALID_PAYMENT;
import static com.example.book_your_seat.reservation.ReservationConst.RESERVATION_CAN_NOT_CANCEL;

@RequiredArgsConstructor
@Service
public class ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;

    public Reservation saveReservation(final Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(final CancelReservationRequest request) {
        Reservation reservation = reservationRepository.findById(request.ReservationId())
                .orElseThrow(() -> new IllegalArgumentException(RESERVATION_CAN_NOT_CANCEL));
        reservation.cancelReservation();

        Payment payment = paymentRepository.findById(reservation.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException(INVALID_PAYMENT));
        payment.cancelPayment();

        List<Seat> seats = seatRepository.findAllById(reservation.getSeatIds());
        seats.parallelStream().forEach(Seat::releaseSeat);
    }

}
