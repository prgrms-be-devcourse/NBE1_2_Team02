package com.example.book_your_seat.common.service;

import com.example.book_your_seat.common.entity.ErrorResult;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.book_your_seat.common.constants.Constants.*;

@Service
@RequiredArgsConstructor

public class SlackFacade {

    private final SlackService slackService;


    public void sendSlackErrorMessage(ErrorResult errorResult, HttpServletRequest request)
    {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();

        dataMap.put(EXCEPTION_LOG, errorResult.message());
        dataMap.put(EXCEPTION_URL, request.getRequestURL().toString());
        dataMap.put(EXCEPTION_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern(ERROR_TIME)));
        dataMap.put(EXCEPTION_METHOD, request.getMethod());



        slackService.setErrorMessage(errorResult.code(), dataMap);

    }



    public void sendPaymentSuccessMessage(ConfirmResponse confirmResponse){

        LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
        dataMap.put("예약 번호", confirmResponse.getReservationId().toString());
        dataMap.put("총 가격", confirmResponse.getConcludePrice().toString());
        dataMap.put("예약 상태", confirmResponse.getStatus().toString());
        dataMap.put("콘서트 제목", confirmResponse.getConcertTitle());
        dataMap.put("콘서트 시간", String.valueOf(confirmResponse.getConcertStartHour()));
        dataMap.put("결제 시간", LocalDateTime.now().format(DateTimeFormatter.ofPattern(ERROR_TIME)));

        String seatNumber = seatNumberConcat(confirmResponse);

        dataMap.put("예약한 콘서트 좌석", seatNumber);

        slackService.setPaymentMessage("결제 완료!", dataMap);

    }

    private static String seatNumberConcat(ConfirmResponse confirmResponse) {
        List<Long> seatsId = confirmResponse.getSeatsId();

        StringBuilder stringBuilder = new StringBuilder();

        for(Long seatId : seatsId){
            stringBuilder.append(seatId).append(" ");
        }

      return stringBuilder.toString();
    }
}
