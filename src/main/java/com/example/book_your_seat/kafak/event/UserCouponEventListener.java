package com.example.book_your_seat.kafak.event;

import com.example.book_your_seat.kafak.outbox.CouponOutbox;
import com.example.book_your_seat.kafak.outbox.OutboxRepository;
import com.example.book_your_seat.kafak.producer.CouponCreateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserCouponEventListener {

    private final CouponCreateProducer couponCreateProducer;
    private final OutboxRepository outboxRepository;

    @TransactionalEventListener(value = UserCouponEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutboxInfo(UserCouponEvent userCouponEvent) {
        outboxRepository.save(new CouponOutbox(userCouponEvent.userId(), userCouponEvent.couponId()));
    }

    @Async
    @TransactionalEventListener(value = UserCouponEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void produceMessage(UserCouponEvent userCouponEvent) {
        couponCreateProducer.create(userCouponEvent);
    }

}
