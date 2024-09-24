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

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private final List<UserCoupon> userCoupons = new ArrayList<>();

    public Coupon(int amount, DiscountRate discountRate) {
        this.amount = amount;
        this.discountRate = discountRate;
    }
}
