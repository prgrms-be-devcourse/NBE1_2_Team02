package com.example.book_your_seat.coupon.domain;

import java.util.Arrays;
import lombok.Getter;

import static com.example.book_your_seat.coupon.CouponConst.NOT_VALID_DISCOUNT_RATE;
import static com.example.book_your_seat.coupon.CouponConst.PERCENT;

@Getter
public enum DiscountRate {
    FIVE(5),
    TEN(10),
    FIFTEEN(15),
    TWENTY(20);

    private final int value;

    DiscountRate(int value) {
        this.value = value;
    }

    public static DiscountRate findBy(int discountRate) {
        return Arrays.stream(DiscountRate.values())
                .filter(it -> it.value == discountRate)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_VALID_DISCOUNT_RATE));
    }

    public String getStringForm() {
        return this.value + PERCENT;
    }
}
