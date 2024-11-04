package com.example.book_your_seat.coupon.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    private boolean isUsed;

    private Long userId;

    private Long couponId;

    public UserCoupon(Long couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
        this.isUsed = false;
    }

    public void setUsed() {
        isUsed = true;
    }
}
