package com.example.book_your_seat.kafak.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponOutbox {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_outbox_id")
    private Long id;

    private Long userId;
    private Long couponId;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    public CouponOutbox(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
        this.status = OutboxStatus.PUBLISHED;
    }
}
