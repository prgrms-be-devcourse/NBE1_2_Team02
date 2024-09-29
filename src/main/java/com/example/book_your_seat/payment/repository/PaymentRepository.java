package com.example.book_your_seat.payment.repository;

import com.example.book_your_seat.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}