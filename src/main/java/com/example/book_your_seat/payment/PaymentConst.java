package com.example.book_your_seat.payment;

import java.time.Duration;

public final class PaymentConst {

    public static final Duration EXPIRE_SECONDS = Duration.ofSeconds(30);
    public static final String FAIL_PAYMENT = "토스 api 결제 실패!";

    private PaymentConst() {
    }
}
