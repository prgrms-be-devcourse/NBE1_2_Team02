package com.example.book_your_seat.payment.service;

import com.example.book_your_seat.payment.controller.dto.request.ConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.repository.PaymentRepository;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public ConfirmResponse processPayment(Long concertId, Long userId, ConfirmRequest request) {
        return null;
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
