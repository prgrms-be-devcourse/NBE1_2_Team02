package com.example.book_your_seat.queue;

public final class QueueConst {
    public static final String PROCESSING = "PROCESSING";
    public static final String WAITING = "WAITING";
    public static final String NOT_IN_QUEUE = "NOT_IN_QUEUE";

    public static final Integer PROCESSING_QUEUE_SIZE = 1000;
    public static final Integer PROCESSING_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000; //30분

    public static final String PROCESSING_QUEUE_KEY = "processing_queue_key";
    public static final String WAITING_QUEUE_KEY = "waiting_queue_key";
    public static final String ALREADY_ISSUED_USER = "이미 토큰을 발급받은 유저입니다.";

}
