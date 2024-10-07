package com.example.book_your_seat.common.exhandler;

import com.example.book_your_seat.common.entity.ErrorResult;
import com.example.book_your_seat.common.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor

public class ExControllerAdvice {

    private final SlackService slackService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> catchError(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);

        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        sendSlackErrorMessage(errorResult);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    private void sendSlackErrorMessage(ErrorResult errorResult)
     {
        HashMap<String, String> dataMap = new HashMap<>();

        dataMap.put("에러 로그", errorResult.message());

        slackService.setErrorMessage(errorResult.code(), dataMap);

    }
}
