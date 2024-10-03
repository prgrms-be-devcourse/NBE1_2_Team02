package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.request.ReserveRequest;
import com.example.book_your_seat.payment.controller.dto.request.TossConfirmCommand;
import com.example.book_your_seat.payment.controller.dto.response.ReserveResponse;
import com.example.book_your_seat.payment.service.dto.ReserveCommand;
import com.example.book_your_seat.payment.service.facade.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservations")
public class PaymentController {

    private final TossApiService tossApiService;
    private final PaymentService paymentService;

    @PostMapping("/{concertId}/{userId}")
    public ResponseEntity<ReserveResponse> reserveSeat(
            @PathVariable final Long concertId,
            @PathVariable final Long userId,
            @RequestBody final ReserveRequest request
    ) {
        ReserveCommand command = ReserveCommand.from(concertId, userId, request);
        ReserveResponse response = paymentService.reserve(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/success")
    public ResponseEntity<Void> confirmPayment(
            @RequestParam final String paymentKey,
            @RequestParam final String orderId,
            @RequestParam final Long amount
    ) {
        TossConfirmCommand command = TossConfirmCommand.from(paymentKey, orderId, amount);
        tossApiService.confirm(command);
        paymentService.processPayment(paymentKey, orderId, amount);
        return ResponseEntity.noContent().build();
    }

}
