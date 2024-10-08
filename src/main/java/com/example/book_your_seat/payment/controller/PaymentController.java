package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.request.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.request.TossConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.controller.dto.response.FinalPriceResponse;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import com.example.book_your_seat.payment.service.dto.PaymentCommand;
import com.example.book_your_seat.payment.service.facade.PaymentFacade;
import com.example.book_your_seat.reservation.contorller.dto.PaymentRequest;
import com.example.book_your_seat.seat.service.redis.SeatRedisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservations")
public class PaymentController {

    private final TossApiService tossApiService;
    private final PaymentFacade paymentFacade;
    private final SeatRedisService seatRedisService;

    @PostMapping("/totalPrice")
    public ResponseEntity<FinalPriceResponse> getTotalPrice(
            @Valid @RequestBody final FinalPriceRequest request
    ) {
        FinalPriceResponse finalPrice = paymentFacade.getFinalPrice(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(finalPrice);
    }

    @PostMapping("/success")
    public ResponseEntity<ConfirmResponse> confirmPayment(@Valid @RequestBody final PaymentRequest request) {

        seatRedisService.validateSeat(request);

        TossConfirmResponse confirmResponse = tossApiService.confirm(TossConfirmRequest.from(request));

        PaymentCommand command = PaymentCommand.from(request, confirmResponse);
        ConfirmResponse response = paymentFacade.processPayment(command);
        return ResponseEntity.ok(response);
    }
}
