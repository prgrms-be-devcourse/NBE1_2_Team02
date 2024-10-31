package com.example.book_your_seat.concert.controller;

import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.service.command.ConcertCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/concerts")
@RestController
public class AdminConcertController {

    private final ConcertCommandService concertCommandService;

    @PostMapping
    public ResponseEntity<Void> addConcert(
            @Valid @RequestBody final AddConcertRequest request
    ) {
        concertCommandService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{concertId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Long concertId) {
        concertCommandService.delete(concertId);
        return ResponseEntity.noContent().build();
    }

}
