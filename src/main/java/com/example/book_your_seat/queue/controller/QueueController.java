package com.example.book_your_seat.queue.controller;

import com.example.book_your_seat.queue.controller.dto.QueueToken;
import com.example.book_your_seat.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<QueueToken> issueQueueToken(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .ok()
                .body(queueService.issueQueueToken(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<Void> getTokenStatus(String token) {
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteQueueToken(String token) {
        return ResponseEntity
                .ok()
                .build();
    }

}
