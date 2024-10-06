package com.example.book_your_seat.payment.service.dto;

import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import com.example.book_your_seat.reservation.contorller.dto.PaymentRequest;

import java.time.LocalDateTime;
import java.util.List;

public final class PaymentCommand {

    public final String orderId;
    public final Long totalAmount;
    public final String paymentKey;
    public final LocalDateTime approvedAt;
    public final List<Long> seatIds;
    public final Long addressId;
    public final Long userId;
    public final Long userCouponId;
    public final Long concertId;

    private PaymentCommand(PaymentRequest request, TossConfirmResponse confirmResponse) {

        Long requestAmount = request.amount();
        Long confirmAmount = confirmResponse.totalAmount();
        if (!requestAmount.equals(confirmAmount)) {
            throw new IllegalArgumentException("INVALID_AMOUNT");
        }

        this.orderId = confirmResponse.orderId();
        this.totalAmount = confirmResponse.totalAmount();
        this.paymentKey = confirmResponse.paymentKey();
        this.approvedAt = confirmResponse.approvedAt();
        this.seatIds = request.seatIds();
        this.addressId = request.addressId();
        this.userId = request.userId();
        this.userCouponId = request.userCouponId();
        this.concertId = request.concertId();
    }

    public static PaymentCommand from(PaymentRequest request, TossConfirmResponse confirmResponse) {
        return new PaymentCommand(request, confirmResponse);
    }

}
