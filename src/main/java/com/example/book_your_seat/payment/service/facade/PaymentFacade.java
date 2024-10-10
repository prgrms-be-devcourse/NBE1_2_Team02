package com.example.book_your_seat.payment.service.facade;

import com.example.book_your_seat.payment.controller.dto.request.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.controller.dto.response.FinalPriceResponse;
import com.example.book_your_seat.payment.service.dto.PaymentCommand;
import org.springframework.stereotype.Service;

@Service
public interface PaymentFacade {
    ConfirmResponse processPayment(PaymentCommand paymentCommand, Long userId, String token);

    FinalPriceResponse getFinalPrice(FinalPriceRequest request);

}
