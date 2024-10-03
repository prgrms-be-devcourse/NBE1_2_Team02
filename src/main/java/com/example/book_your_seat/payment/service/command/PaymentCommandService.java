package com.example.book_your_seat.payment.service.command;

import com.example.book_your_seat.payment.controller.dto.PaymentDto;
import com.example.book_your_seat.payment.domain.Payment;

public interface PaymentCommandService {
    Payment createPayment(final PaymentDto paymentDto);
}
