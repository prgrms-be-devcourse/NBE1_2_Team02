package com.example.book_your_seat.queue.controller;

import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.TokenResponse;
import com.example.book_your_seat.queue.service.command.QueueCommandService;
import com.example.book_your_seat.queue.service.query.QueueQueryService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueCommandService queueCommandService;
    private final QueueQueryService queueQueryService;

    @PostMapping("/token/{concertId}")
    public ResponseEntity<TokenResponse> issueTokenAndEnqueue(@LoginUser User user, @PathVariable("concertId") Long concertId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(queueCommandService.issueTokenAndEnqueue(user.getId(), concertId));
    }

    @GetMapping("/{concertId}")
    public ResponseEntity<QueueResponse> getQueueInfoWithToken(@PathVariable("concertId") Long concertId,
                                                               @RequestParam("token") String token) {
        return ResponseEntity.ok()
                .body(queueQueryService.findQueueStatus(concertId, token));
    }

    @PostMapping("/wait/quit/{concertId}")
    public ResponseEntity<Void> dequeueWaitingQueue(@LoginUser User user, @PathVariable("concertId") Long concertId,
                                                    @RequestParam("token") String token) {
        queueCommandService.removeTokenInWaitingQueue(user.getId(), concertId, token);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/process/quit/{concertId}")
    public ResponseEntity<Void> dequeueProcessingQueue(@LoginUser User user, @PathVariable("concertId") Long concertId,
                                                       @RequestParam("token") String token) {
        queueCommandService.removeTokenInProcessingQueue(user.getId(), concertId, token);
        return ResponseEntity.ok(null);
    }
}
