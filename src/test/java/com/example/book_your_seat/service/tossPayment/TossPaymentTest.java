package com.example.book_your_seat.service.tossPayment;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.payment.controller.dto.request.TossConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


class TossPaymentTest extends IntegralTestSupport {


    @Test
    @DisplayName("토스페이먼츠 결재 확정 api 성공")
    void testConfirm() {
        //given
        TossConfirmRequest request = new TossConfirmRequest(
                "paymentKey", "orderId", 1000L
        );
        TossConfirmResponse mockResponse = new TossConfirmResponse(
                "orderId", 1000L, "paymentKey", LocalDateTime.now()
        );

        when(tossApiService.confirm(request)).thenReturn(mockResponse);

        //when
        TossConfirmResponse response = tossApiService.confirm(request);

        //then
        assertThat(response, Matchers.equalTo(mockResponse));
    }

    @Test
    @DisplayName("토스페이먼츠 결재 확정 api 실패")
    void testConfirmException() {
        //given
        TossConfirmRequest request = new TossConfirmRequest(
                "paymentKey", "orderId", 1000L
        );

        when(tossApiService.confirm(request))
                .thenThrow(new IllegalArgumentException());

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            tossApiService.confirm(request);
        });
    }
}
