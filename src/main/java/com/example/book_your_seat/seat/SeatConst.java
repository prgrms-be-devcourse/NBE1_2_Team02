package com.example.book_your_seat.seat;

public final class SeatConst {


    public static final String INVALID_ADDRESS = "addressId를 입력하세요";
    public static final String ENTER_SEATS = "seats를 입력하세요";

    public static final String SEAT_SOLD = "이미 예약된 좌석 입니다.";
    public static final String REDISSON_LOCK_KEY = "LOCK_SEAT:";

    public static final String ACCEPTABLE_TIMEOUT = "결재 가능 시간을 초과 했습니다.";

    public static final int SPECIAL_RATIO = 3;
    public static final int PREMIUM_RATIO = 2;
    public static final int NORMAL_RATIO = 1;
}
