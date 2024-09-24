package com.example.book_your_seat.common.exhandler;

import com.example.book_your_seat.common.entity.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> catchError(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);

        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
