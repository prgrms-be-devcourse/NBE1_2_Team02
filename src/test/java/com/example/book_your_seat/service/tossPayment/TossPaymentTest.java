package com.example.book_your_seat.service.tossPayment;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.payment.controller.TossApiService;
import com.example.book_your_seat.payment.controller.TossOpenFeign;
import com.example.book_your_seat.payment.controller.dto.request.TossConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Base64;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TossPaymentTest extends IntegralTestSupport {

    @MockBean
    private TossOpenFeign tossOpenFeign;

    @Autowired
    private TossApiService tossApiService;

    // 토스에서 정상 응답 시 결제 되는지
    // Reservation Test 까지

    @Test
    @DisplayName("토스페이먼츠 결재 확정 api 성공")
    public void testConfirm() {
        //given
        TossConfirmRequest request = new TossConfirmRequest(
                "paymentKey", "orderId", 1000L
        );
        TossConfirmResponse mockResponse = new TossConfirmResponse(
                "orderId", 1000L, "paymentKey", LocalDateTime.now()
        );
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(("1234" + ":").getBytes());
        when(tossApiService.confirm(request)).thenReturn(mockResponse);

        //when
        TossConfirmResponse response = tossApiService.confirm(request);

        //then
        assertThat(response, Matchers.equalTo(mockResponse));
    }

    @Test
    @DisplayName("토스페이먼츠 결재 확정 api 실패")
    public void testConfirmException() {
        //given
        TossConfirmRequest request = new TossConfirmRequest(
                "paymentKey", "orderId", 1000L
        );
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(("SECRET_KEY" + ":").getBytes());

        when(tossApiService.confirm(request))
                .thenThrow(new IllegalArgumentException());

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            tossApiService.confirm(request);
        });
    }
}
