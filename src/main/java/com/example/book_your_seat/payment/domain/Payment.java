package com.example.book_your_seat.payment.domain;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Integer totalPrice;
    private LocalDateTime expiryAt;
    @Enumerated(EnumType.STRING)
    private DiscountRate discountRate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String tossPaymentOrderId;

    private String tossPaymentPaymentKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Builder
    public Payment(Integer totalPrice, LocalDateTime expiryAt, DiscountRate discountRate, PaymentStatus paymentStatus,String tossPaymentOrderId, String TossPaymentPaymentKey) {
        this.totalPrice = totalPrice;
        this.expiryAt = expiryAt;
        this.discountRate = discountRate;
        this.paymentStatus = paymentStatus;
        this.tossPaymentOrderId = tossPaymentOrderId;
        this.tossPaymentPaymentKey = TossPaymentPaymentKey;
    }

    public void addReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
