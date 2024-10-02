package com.example.book_your_seat.queue.controller;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.TokenResponse;
import com.example.book_your_seat.queue.service.QueueCommandService;
import com.example.book_your_seat.queue.service.QueueQueryService;
import com.example.book_your_seat.queue.service.facade.QueueService;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.book_your_seat.common.SessionConst.LOGIN_USER;

@RestController
@RequestMapping("/api/v1/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> issueTokenAndEnqueue(@SessionAttribute(LOGIN_USER) User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(queueService.issueTokenAndEnqueue(user.getId()));
    }

    @GetMapping("/queue")
    public ResponseEntity<QueueResponse> getQueueInfoWithToken(@SessionAttribute(LOGIN_USER) User user,
                                                               @RequestParam("token") String token)  {
        return ResponseEntity.ok()
                .body(queueService.findQueueStatus(user.getId(), token));
    }

    @PostMapping("/quit")
    public ResponseEntity<Void> dequeueWaitingQueue(@SessionAttribute(LOGIN_USER) User user,
                                                    @RequestParam("token") String token) {
        queueService.dequeueWaitingQueue(user.getId(), token);
        return ResponseEntity.ok(null);
    }
}
