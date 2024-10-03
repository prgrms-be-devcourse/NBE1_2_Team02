package com.example.book_your_seat.payment.service.facade;

import com.example.book_your_seat.payment.controller.dto.response.ReserveResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.service.dto.ReserveCommand;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    Payment findByPaymentId(String paymentId);
    ReserveResponse reserve(ReserveCommand command);
    void processPayment(String paymentKey, String orderId, Long amount);
}
