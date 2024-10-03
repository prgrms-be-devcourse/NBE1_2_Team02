package com.example.book_your_seat.seat;

public final class SeatConst {

    public static final String ENTER_USER_ID = "userId를 입력하세요!";

    public static final String ENTER_CONCERT_ID = "concertID를 입력하세요!";

    public static final String ENTER_ADDRESS_ID = "addressId를 입력하세요";

    public static final String ENTER_SEATS = "seats를 입력하세요";

    public static final String ENTER_PRICE = "price를 입력하세요";

    public static final String SEAT_SOLD = "이미 예약된 좌석 입니다.";
    public static final String SEAT_NOT_FOUND = "해당하는 좌석이 없습니다.";
    public static final String REDISSON_LOCK_KEY = "LOCK_SEAT:";

    public static final String ACCEPTABLE_TIMEOUT = "결재 가능 시간을 초과 했습니다.";
}
