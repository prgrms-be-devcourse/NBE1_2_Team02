package com.example.book_your_seat.concert.controller;

import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.concert.service.ConcertQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/concerts")
@RestController
public class ConcertController {

    private final ConcertCommandService concertCommandService;
    private final ConcertQueryService concertQueryService;

    @GetMapping
    public ResponseEntity<List<ConcertResponse>> findAll() {
        List<ConcertResponse> responses = concertQueryService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertResponse> findById(@PathVariable final Long concertId) {
        ConcertResponse response = concertQueryService.findById(concertId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ConcertResponse> addConcert(
            @Valid @RequestBody final AddConcertRequest request
    ) {
        ConcertResponse response = concertCommandService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{concertId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Long concertId) {
        concertCommandService.delete(concertId);
        return ResponseEntity.noContent().build();
    }
}
