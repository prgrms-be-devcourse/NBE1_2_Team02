package com.example.book_your_seat.payment.service.command.impl;

import com.example.book_your_seat.payment.controller.dto.PaymentDto;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.repository.PaymentRepository;
import com.example.book_your_seat.payment.service.command.PaymentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.payment.domain.PaymentStatus.COMPLETED;

@Component
@Transactional
@RequiredArgsConstructor
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(PaymentDto paymentDto) {
        Payment payment = Payment.builder()
                .totalPrice(paymentDto.totalPrice())
                .expiryAt(paymentDto.executedAt())
                .paymentStatus(COMPLETED)
                .discountRate(paymentDto.discountRate())
                .tossPaymentOrderId(paymentDto.orderId())
                .TossPaymentPaymentKey(paymentDto.paymentKey())
                .build();

        return paymentRepository.save(payment);
    }
}
