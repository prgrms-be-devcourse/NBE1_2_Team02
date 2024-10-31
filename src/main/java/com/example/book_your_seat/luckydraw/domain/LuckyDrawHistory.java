package com.example.book_your_seat.luckydraw.domain;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LuckyDrawHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lucky_draw_history")
    private Long id;

    private Long userId;
    private DiscountRate discountRate;

    public LuckyDrawHistory(Long userId, DiscountRate discountRate) {
        this.userId = userId;
        this.discountRate = discountRate;
    }
}
