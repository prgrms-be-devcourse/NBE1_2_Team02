package com.example.book_your_seat.concert.controller;

import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.controller.dto.ResultRedisConcert;
import com.example.book_your_seat.concert.service.ConcertCommandService;
import com.example.book_your_seat.concert.service.ConcertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
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

    @DeleteMapping("/{concertId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Long concertId) {
        concertCommandService.delete(concertId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/redis/list")
    public ResponseEntity<ResultRedisConcert> findAllRedisList(){
        ResultRedisConcert resultRedisConcert = concertQueryService.findUsedRedisAllConcertList();
        return ResponseEntity.ok(resultRedisConcert);
    }
}
