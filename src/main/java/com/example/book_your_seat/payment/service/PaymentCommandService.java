package com.example.book_your_seat.payment.service;

import com.example.book_your_seat.payment.controller.dto.request.ConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.reservation.domain.Reservation;
import org.springframework.stereotype.Service;

@Service
public interface PaymentCommandService {
    ConfirmResponse processPayment(Long concertId, Long userId, ConfirmRequest request);
    Reservation saveReservation(Reservation reservation);
    Payment savePayment(Payment payment);
}
