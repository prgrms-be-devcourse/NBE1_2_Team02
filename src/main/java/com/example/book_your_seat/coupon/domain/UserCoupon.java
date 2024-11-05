package com.example.book_your_seat.coupon.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(indexes = {
        @Index(name = "idx_user_coupon_user_id", columnList = "user_id"),
        @Index(name = "idx_user_coupon_coupon_id", columnList = "coupon_id")
})
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
