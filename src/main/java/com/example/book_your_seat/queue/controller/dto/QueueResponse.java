package com.example.book_your_seat.queue.controller.dto;

import static com.example.book_your_seat.queue.QueueConst.ZERO;

import com.example.book_your_seat.queue.util.QueueStatus;

public record QueueResponse(
        QueueStatus status,
        Long remainingWaitingCount,
        Long estimatedWaitTime
) {
    public static QueueResponse processing() {
        return new QueueResponse(QueueStatus.PROCESSING, ZERO, ZERO);
    }

    public static QueueResponse waiting(Long position, Long estimatedWaitTime) {
        return new QueueResponse(QueueStatus.WAITING, position, estimatedWaitTime);
    }

    public static QueueResponse cancel() {
        return new QueueResponse(QueueStatus.CANCELED, ZERO, ZERO);
    }
}
