package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.FinalPriceResponse;
import com.example.book_your_seat.payment.controller.dto.PaymentSuccessRequest;
import com.example.book_your_seat.payment.controller.dto.PaymentSuccessResponse;
import com.example.book_your_seat.payment.service.command.PaymentCommandService;
import com.example.book_your_seat.payment.service.facade.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentCommandService paymentCommandService;
    private final PaymentService paymentService;
    @PostMapping("/success")
    public ResponseEntity<PaymentSuccessResponse> paymentSuccess(@Valid @RequestBody final PaymentSuccessRequest request) {
        PaymentSuccessResponse paymentSuccessResponse = paymentCommandService.paymentSuccess(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentSuccessResponse);
    }

    @PostMapping("/totalPrice")
    public ResponseEntity<FinalPriceResponse> getTotalPrice(@Valid @RequestBody final FinalPriceRequest request) {
        FinalPriceResponse finalPrice = paymentService.getFinalPrice(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(finalPrice);
    }
}
