package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.common.service.SlackFacade;
import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.payment.controller.dto.request.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.request.TossCancelRequest;
import com.example.book_your_seat.payment.controller.dto.request.TossConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.controller.dto.response.FinalPriceResponse;
import com.example.book_your_seat.payment.controller.dto.response.TossCancelResponse;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import com.example.book_your_seat.payment.service.dto.PaymentCommand;
import com.example.book_your_seat.payment.service.facade.PaymentFacade;
import com.example.book_your_seat.reservation.contorller.dto.request.CancelReservationRequest;
import com.example.book_your_seat.reservation.contorller.dto.request.PaymentRequest;
import com.example.book_your_seat.reservation.service.facade.ReservationFacade;
import com.example.book_your_seat.seat.redis.SeatRedisService;
import com.example.book_your_seat.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservations")
public class PaymentController {

    private final TossApiService tossApiService;
    private final PaymentFacade paymentFacade;
    private final SeatRedisService seatRedisService;
    private final SlackFacade slackFacade;
    private final ReservationFacade reservationFacade;

    @Operation(
            summary = "최종 가격을 가져옵니다.",
            description = "최종 가격을 가져옵니다."
    )
    @PostMapping("/totalPrice")
    public ResponseEntity<FinalPriceResponse> getTotalPrice(
            @Valid @RequestBody final FinalPriceRequest request
    ) {
        FinalPriceResponse finalPrice = paymentFacade.getFinalPrice(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(finalPrice);
    }

    @Operation(
            summary = "토스페이먼츠에 결재 승인 요청을 보냅니다.",
            description = "토스페이먼츠에 결재 승인 요청을 보냅니다."
    )
    @PostMapping("/success")
    public ResponseEntity<ConfirmResponse> confirmPayment(
            @Valid @RequestBody final PaymentRequest request,
            @LoginUser User user,
            @RequestParam("token") String token
    ) {

        seatRedisService.validateSeat(request, user.getId());

        TossConfirmResponse confirmResponse = tossApiService.confirm(TossConfirmRequest.from(request));

        PaymentCommand command = PaymentCommand.from(request, confirmResponse);
        ConfirmResponse response = paymentFacade.processPayment(command, user.getId(), token);

        slackFacade.sendPaymentSuccessMessage(response);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "예매 취소를 요청합니다.",
            description = "예매 취소를 요청합니다."
    )
    @PutMapping("/cancel")
    public ResponseEntity<TossCancelResponse> cancelReservation(
            @Valid @RequestBody final CancelReservationRequest request,
            @LoginUser User user,
            @RequestParam("token") String token
    ){
        String paymentKey = reservationFacade.getValidPaymentKey(request);

        TossCancelResponse response = tossApiService.cancel(paymentKey, new TossCancelRequest("고객 요청"));
        reservationFacade.cancelReservation(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
