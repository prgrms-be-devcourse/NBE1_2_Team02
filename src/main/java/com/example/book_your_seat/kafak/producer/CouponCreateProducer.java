package com.example.book_your_seat.kafak.producer;

import com.example.book_your_seat.kafak.event.UserCouponEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateProducer {

    private final KafkaTemplate<String, UserCouponEvent> kafkaTemplate;

    public CouponCreateProducer(KafkaTemplate<String, UserCouponEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create(UserCouponEvent userCouponEvent) {
        kafkaTemplate.send("issue-coupon", userCouponEvent);
    }
}
