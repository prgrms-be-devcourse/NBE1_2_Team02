package com.example.book_your_seat.common.exhandler;

import com.example.book_your_seat.common.entity.ErrorResult;
import com.example.book_your_seat.common.service.SlackFacade;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor

public class ExControllerAdvice {

    private final SlackFacade slackFacade;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> catchError(IllegalArgumentException e, HttpServletRequest request) {
        log.error("[exceptionHandle] ex", e);

        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        slackFacade.sendSlackErrorMessage(errorResult, request);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }


}
