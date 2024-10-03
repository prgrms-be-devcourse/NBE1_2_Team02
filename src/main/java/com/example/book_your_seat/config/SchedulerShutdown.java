package com.example.book_your_seat.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SchedulerShutdown {

    private ThreadPoolTaskScheduler taskScheduler;

    @PreDestroy
    public void shutdown() {
        if (taskScheduler != null) {
            taskScheduler.shutdown();
        }
    }
}
