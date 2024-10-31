package com.example.book_your_seat.payment.service.command;

import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentCommandService {

    private final PaymentRepository paymentRepository;


    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
