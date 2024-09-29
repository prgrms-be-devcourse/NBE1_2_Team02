package com.example.book_your_seat.payment.service.command;

import com.example.book_your_seat.payment.controller.dto.PaymentSuccessRequest;
import com.example.book_your_seat.payment.controller.dto.PaymentSuccessResponse;

public interface PaymentCommandService {
    PaymentSuccessResponse paymentSuccess(final PaymentSuccessRequest request);
}
