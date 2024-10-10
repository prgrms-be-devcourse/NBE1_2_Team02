package com.example.book_your_seat.seat.domain;

import java.util.function.Function;

import static com.example.book_your_seat.seat.SeatConst.*;

public enum Zone {

    SPECIAL(price -> price * SPECIAL_RATIO),
    PREMIUM(price -> price * PREMIUM_RATIO),
    NORMAL(price -> price * NORMAL_RATIO);

    private Function<Integer, Integer> expression;

    Zone(Function<Integer, Integer> expression) {
        this.expression = expression;
    }

    public int applyZonePrice(int price) {
        return expression.apply(price);
    }

    public static Zone setZone(int seatNumber) {
        if (seatNumber <= 30) {
            return SPECIAL;
        }
        if (seatNumber <= 60) {
            return PREMIUM;
        }
        return NORMAL;
    }
}
