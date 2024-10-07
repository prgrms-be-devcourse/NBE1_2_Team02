package com.example.book_your_seat.common.exhandler;

import com.example.book_your_seat.common.entity.ErrorResult;
import com.example.book_your_seat.common.service.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import static com.example.book_your_seat.common.constants.Constants.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor

public class ExControllerAdvice {

    private final SlackService slackService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> catchError(IllegalArgumentException e, HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        sendSlackErrorMessage(errorResult, request);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    private void sendSlackErrorMessage(ErrorResult errorResult, HttpServletRequest request)
     {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();

        dataMap.put(EXCEPTION_LOG, errorResult.message());
        dataMap.put(EXCEPTION_URL, request.getRequestURL().toString());
        dataMap.put(EXCEPTION_DATETIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern(ERROR_TIME)));
        dataMap.put(EXCEPTION_METHOD, request.getMethod());



        slackService.setErrorMessage(errorResult.code(), dataMap);

    }
}
