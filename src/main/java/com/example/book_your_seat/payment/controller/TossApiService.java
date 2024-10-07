package com.example.book_your_seat.payment.controller;

import com.example.book_your_seat.payment.controller.dto.request.TossConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.response.TossConfirmResponse;
import com.example.book_your_seat.payment.controller.dto.response.TossErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Base64;

@RequiredArgsConstructor
@Service
public class TossApiService {

    private final TossOpenFeign tossOpenFeign;
    private final ObjectMapper objectMapper;
    private static final String SECRET_KEY = "key";

    public TossConfirmResponse confirm(TossConfirmRequest request) {
        String auth = Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());

        try {
            return tossOpenFeign.confirm("Basic " + auth, request);
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {
            handleTossException(httpException);
            return null;
        }
    }

    private void handleTossException(HttpStatusCodeException httpException) {
        try {
            TossErrorResponse response = objectMapper.readValue(httpException.getResponseBodyAsString(), TossErrorResponse.class);
            throw new IllegalArgumentException(response.message());
        } catch (JsonProcessingException jsonException) {
            throw new IllegalArgumentException(jsonException.getMessage());
        }
    }
}
