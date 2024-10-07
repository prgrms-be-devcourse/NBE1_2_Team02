package com.example.book_your_seat.payment.service;

import com.example.book_your_seat.payment.domain.Payment;
import org.springframework.stereotype.Service;

@Service
public interface PaymentCommandService {

    Payment savePayment(Payment payment);
}
