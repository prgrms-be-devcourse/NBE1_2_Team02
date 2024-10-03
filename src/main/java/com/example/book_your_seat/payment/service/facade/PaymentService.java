package com.example.book_your_seat.payment.service.facade;

import com.example.book_your_seat.payment.controller.dto.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.FinalPriceResponse;

public interface PaymentService {
    FinalPriceResponse getFinalPrice(FinalPriceRequest request);
}
