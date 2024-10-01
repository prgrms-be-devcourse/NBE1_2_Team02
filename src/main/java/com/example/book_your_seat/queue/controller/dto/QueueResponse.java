package com.example.book_your_seat.queue.controller.dto;

import com.example.book_your_seat.queue.domain.QueueStatus;

public record QueueResponse(
        QueueStatus status,
        Long remainingWaitingCount,
        Long estimatedWaitTime
) {
    public static QueueResponse processing() {
        return new QueueResponse(QueueStatus.PROCESSING, 0L, 0L);
    }

    public static QueueResponse waiting(Long position, Long estimatedWaitTime) {
        return new QueueResponse(QueueStatus.WAITING, position, estimatedWaitTime);
    }
}
