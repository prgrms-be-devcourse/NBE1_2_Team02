package com.example.book_your_seat.payment.controller.dto;

import java.math.BigDecimal;

public record FinalPriceResponse(
        BigDecimal finalPrice
) {

}
