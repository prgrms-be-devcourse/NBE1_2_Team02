package com.example.book_your_seat.payment.controller.dto.response;

import java.util.UUID;

public record ReserveResponse(

        UUID paymentId,
        Long concludePrice
) {
}
