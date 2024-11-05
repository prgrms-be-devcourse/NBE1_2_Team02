package com.example.book_your_seat.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.book_your_seat.payment.domain.PaymentStatus.CANCELLED;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID id;

    private Long totalPrice;
    private LocalDateTime expiryAt;
    private String discountRate;
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    @Builder
    public Payment(Long totalPrice, LocalDateTime expiryAt, String discountRate, PaymentStatus paymentStatus, String paymentKey) {
        this.totalPrice = totalPrice;
        this.expiryAt = expiryAt;
        this.discountRate = discountRate;
        this.paymentStatus = paymentStatus;
        this.paymentKey = paymentKey;
    }


    public void cancelPayment() {
        this.paymentStatus = CANCELLED;
    }
}
