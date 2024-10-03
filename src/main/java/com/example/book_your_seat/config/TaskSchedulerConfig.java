package com.example.book_your_seat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static com.example.book_your_seat.common.constants.Constants.SEAT_STATUS_POOL_SIZE;
import static com.example.book_your_seat.common.constants.Constants.TEN_MINUTE_BY_SECOND;

@Configuration
public class TaskSchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler schedule() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setWaitForTasksToCompleteOnShutdown(false);
        scheduler.setPoolSize(SEAT_STATUS_POOL_SIZE);
        scheduler.setAwaitTerminationSeconds(TEN_MINUTE_BY_SECOND);

        return scheduler;
    }

}
