package com.example.book_your_seat.kwanseTest;

import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ConcertTestController {

    private final ConcertCommandService concertCommandService;

    @PostMapping("/concert/test")
    public ResponseEntity<Void> addConcert(
            @RequestBody final AddConcertRequest request
    ) {
        concertCommandService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
