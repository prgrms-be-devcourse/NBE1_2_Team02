package com.example.book_your_seat.payment.controller.dto.request;

public final class TossConfirmCommand {

    private final String paymentKey;
    private final String orderId;
    private final Long amount;


    private TossConfirmCommand(String paymentKey, String orderId, Long amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

    public static TossConfirmCommand from(String paymentKey, String orderId, Long amount) {
        return new TossConfirmCommand(paymentKey, orderId, amount);
    }
}
