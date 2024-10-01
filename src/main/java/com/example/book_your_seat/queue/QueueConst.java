package com.example.book_your_seat.queue;

public class QueueConst {

    private QueueConst() { }

    public static final String PROCESSING_QUEUE_KEY = "PROCESSING_QUEUE";
    public static final String WAITING_QUEUE_KEY = "WAITING_QUEUE";

    public static final String ALREADY_IN_QUEUE_KEY = "이미 대기열에 있습니다.";

    public static final String DELIMITER = ":";

    public static final Long ZERO = 0L;
    public static final Long FIVE = 5L;
    public static final Long MINUTE = 60L;
    public static final Long ALLOWED_PROCESSING_SIZE = 1000L;
    public static final Long FIFTEEN_MINUTE = 1000 * 60 * 15L;

}
