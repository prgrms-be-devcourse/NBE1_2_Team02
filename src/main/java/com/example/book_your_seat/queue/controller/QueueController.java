package com.example.book_your_seat.queue.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.TokenResponse;
import com.example.book_your_seat.queue.service.facade.QueueService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> issueTokenAndEnqueue(@LoginUser User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(queueService.issueTokenAndEnqueue(user.getId()));
    }

    @GetMapping
    public ResponseEntity<QueueResponse> getQueueInfoWithToken(@LoginUser User user,
                                                               @RequestParam("token") String token)  {
        return ResponseEntity.ok()
                .body(queueService.findQueueStatus(user.getId(), token));
    }

    @PostMapping("/wait/quit")
    public ResponseEntity<Void> dequeueWaitingQueue(@LoginUser User user,
                                                    @RequestParam("token") String token) {
        queueService.dequeueWaitingQueue(user.getId(), token);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/process/quit")
    public ResponseEntity<Void> dequeueProcessingQueue(@LoginUser User user,
                                                    @RequestParam("token") String token) {
        queueService.dequeueProcessingQueue(user.getId(), token);
        return ResponseEntity.ok(null);
    }
}
