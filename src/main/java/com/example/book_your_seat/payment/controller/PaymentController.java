package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.request.TossConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import com.example.book_your_seat.payment.service.dto.PaymentCommand;
import com.example.book_your_seat.payment.service.facade.PaymentService;
import com.example.book_your_seat.reservation.contorller.dto.PaymentRequest;
import com.example.book_your_seat.seat.service.redis.SeatRedisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final PaymentService paymentService;
    private final SeatRedisService seatRedisService;

    @PostMapping("/success")
    public ResponseEntity<Void> confirmPayment(@Valid @RequestBody final PaymentRequest request) {
        //paymentService 하나에 여러 서비스 주입 받아서 -> PaymentController에 paymentService 하나만 주입

        //PaymentController에 여러 서비스 주입바아서 처리

        // Reservation, Payment 생성 위치
        // Reservation - concert, seat, userId
        // concertTitle, List<Long> seatsId, 결제 시각, 공연 시각, 금액

        // getPrice 어떻게 할지,,

        //좌석 유효성 검증
        seatRedisService.validateSeat(request);

        TossConfirmResponse confirmResponse = tossApiService.confirm(TossConfirmRequest.from(request));

        PaymentCommand command = PaymentCommand.from(request, confirmResponse);
        paymentService.processPayment(command);
        return ResponseEntity.noContent().build();
    }

}
