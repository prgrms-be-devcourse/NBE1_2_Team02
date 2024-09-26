package com.example.book_your_seat.coupon.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    //낙관적 락을 위한 버전
    @Version
    private Long version;

    public Coupon(int amount, DiscountRate discountRate, LocalDate expirationDate) {
        this.amount = amount;
        this.discountRate = discountRate;
        this.expirationDate = expirationDate;
    }

    public void addUserCoupon(UserCoupon userCoupon) {
        this.userCoupons.add(userCoupon);
    }

    public void decreaseAmount() {
        this.amount -= 1;
    }
}
