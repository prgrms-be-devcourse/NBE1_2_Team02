package com.example.book_your_seat.kafak.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<CouponOutbox, Long> {
}
