package com.example.book_your_seat.coupon.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

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

    public static DiscountRate findBy(final int discountRate) {
        return Arrays.stream(DiscountRate.values())
                .filter(it -> it.value == discountRate)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_VALID_DISCOUNT_RATE));
    }

    public String getStringForm() {
        return this.value + PERCENT;
    }
    public BigDecimal calculateDiscountedPrice(BigDecimal originalPrice) {
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal discountValue = new BigDecimal(this.value);
        BigDecimal percentage = hundred.subtract(discountValue);

        BigDecimal discountedPercentage = percentage.divide(hundred, 1, RoundingMode.DOWN);
        return originalPrice.multiply(discountedPercentage).setScale(1, RoundingMode.DOWN);
    }
}
