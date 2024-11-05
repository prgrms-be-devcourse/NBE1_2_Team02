package com.example.book_your_seat.concert.controller;

import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.service.command.ConcertCommandService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "콘서트 추가",
            description = "콘서트를 추가합니다."
    )
    public ResponseEntity<Void> addConcert(
            @Valid @RequestBody final AddConcertRequest request
    ) {
        concertCommandService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "콘서트 삭제",
            description = "특정 ID의 콘서트를 삭제합니다."
    )
    @DeleteMapping("/{concertId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Long concertId) {
        concertCommandService.delete(concertId);
        return ResponseEntity.noContent().build();
    }

}
