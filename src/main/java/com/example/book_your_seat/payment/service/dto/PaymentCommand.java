package com.example.book_your_seat.payment.service.dto;

import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import com.example.book_your_seat.reservation.contorller.dto.request.PaymentRequest;
import com.example.book_your_seat.seat.domain.SeatId;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.book_your_seat.payment.PaymentConst.INVALID_AMOUNT;

public final class PaymentCommand {

    public final String orderId;
    public final Long totalAmount;
    public final String paymentKey;
    public final LocalDateTime approvedAt;
    public final List<SeatId> seatIds;
    public final String postCode;
    public final String detail;
    public final Long userCouponId;
    public final Long concertId;

    private PaymentCommand(PaymentRequest request, TossConfirmResponse confirmResponse) {
        Long requestAmount = request.amount();
        Long confirmAmount = confirmResponse.totalAmount();
        if (!requestAmount.equals(confirmAmount)) {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }

        this.orderId = confirmResponse.orderId();
        this.totalAmount = confirmResponse.totalAmount();
        this.paymentKey = confirmResponse.paymentKey();
        this.approvedAt = confirmResponse.approvedAt();
        this.seatIds = request.seatNumbers().stream()
                .map(number -> new SeatId(request.concertId(), number))
                .toList();
        this.userCouponId = request.userCouponId();
        this.concertId = request.concertId();
        this.postCode = request.postCode();
        this.detail = request.detail();
    }

    public static PaymentCommand from(PaymentRequest request, TossConfirmResponse confirmResponse) {
        return new PaymentCommand(request, confirmResponse);
    }

}
