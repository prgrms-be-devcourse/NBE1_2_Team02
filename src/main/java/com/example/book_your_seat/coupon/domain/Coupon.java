package com.example.book_your_seat.coupon.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private int amount;

    @Enumerated(EnumType.STRING)
    private DiscountRate discountRate;

    private LocalDate expirationDate;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private final List<UserCoupon> userCoupons = new ArrayList<>();

    public Coupon(int amount, DiscountRate discountRate, LocalDate expirationDate) {
        this.amount = amount;
        this.discountRate = discountRate;
        this.expirationDate = expirationDate;
    }

    public void addUserCoupon(UserCoupon userCoupon) {
        this.userCoupons.add(userCoupon);
    }

    public void issue() {
        if (this.amount <= 0) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }
        this.amount -= 1;

    }

    public boolean noStock() {
        return this.amount <= 0;
    }
}
