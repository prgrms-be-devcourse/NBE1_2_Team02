package com.example.book_your_seat.payment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID id;

    private Long totalPrice;
    private Long userCouponId;
    private LocalDateTime processedAt;
    private String discountRate;


    @Builder
    public Payment(Long totalPrice,
                   Long userCouponId,
                   String discountRate
    ) {
        this.totalPrice = totalPrice;
        this.userCouponId = userCouponId;
        this.discountRate = discountRate;

    }

    public void setAdditionalInfo() {
        if (this.processedAt == null) {
            this.processedAt = LocalDateTime.now();
        }
    }
}
