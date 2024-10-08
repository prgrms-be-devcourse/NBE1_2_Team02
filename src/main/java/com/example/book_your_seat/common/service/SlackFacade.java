package com.example.book_your_seat.common.service;

import com.example.book_your_seat.common.entity.ErrorResult;
import com.example.book_your_seat.common.entity.color.Color;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.book_your_seat.common.constants.Constants.*;
import static com.example.book_your_seat.concert.ConcertConst.CONCERT_NAME;
import static com.example.book_your_seat.concert.ConcertConst.CONCERT_TIME;
import static com.example.book_your_seat.payment.PaymentConst.AMOUNT_PAY;
import static com.example.book_your_seat.payment.PaymentConst.PAY_TIME;
import static com.example.book_your_seat.reservation.ReservationConst.*;

@Service
@RequiredArgsConstructor

public class SlackFacade {

    private final SlackService slackService;


    public void sendSlackErrorMessage(ErrorResult errorResult, HttpServletRequest request)
    {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();

        dataMap.put(EXCEPTION_LOG, errorResult.message());
        dataMap.put(EXCEPTION_URL, request.getRequestURL().toString());
        dataMap.put(EXCEPTION_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME)));
        dataMap.put(EXCEPTION_METHOD, request.getMethod());



        slackService.setMessage(errorResult.code(), dataMap, Color.RED);

    }



    public void sendPaymentSuccessMessage(ConfirmResponse confirmResponse){

        LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
        dataMap.put(RESERVATION_NUMBER, confirmResponse.getReservationId().toString());
        dataMap.put(AMOUNT_PAY, confirmResponse.getConcludePrice().toString());
        dataMap.put(RESERVATION_STATE, confirmResponse.getStatus().toString());
        dataMap.put(CONCERT_NAME, confirmResponse.getConcertTitle());
        dataMap.put(CONCERT_TIME, String.valueOf(confirmResponse.getConcertStartHour()));
        dataMap.put(PAY_TIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME)));

        String seatNumber = seatNumberConcat(confirmResponse);

        dataMap.put(RESERVATION_SEAT, seatNumber);

        slackService.setMessage("결제 완료!", dataMap, Color.BLUE);

    }

    private static String seatNumberConcat(ConfirmResponse confirmResponse) {
        List<Integer> seatsId = confirmResponse.getSeatNumbers();

        StringBuilder stringBuilder = new StringBuilder();

        for(Integer seatId : seatsId){
            stringBuilder.append("좌석 번호: ").append(seatId).append(" ");
        }

      return stringBuilder.toString();
    }
}
