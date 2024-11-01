package com.example.book_your_seat.likeconcert.controller;

import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.likeconcert.controller.dto.AddLikeRequest;
import com.example.book_your_seat.likeconcert.service.facade.LikeConcertFacade;
import com.example.book_your_seat.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeConcertController {

    private final LikeConcertFacade likeConcertFacade;

    @PostMapping
    public ResponseEntity<Void> like(
            @LoginUser User user,
            @RequestBody AddLikeRequest request
    ) {
        likeConcertFacade.addLike(user.getId(), request.concertId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<List<ConcertListResponse>> findAll(
            @LoginUser User user,
            @PathVariable Long likeId
    ) {
        List<ConcertListResponse> likesByUserId
                = likeConcertFacade.findLikesByUserId(user.getId(), likeId);
        return ResponseEntity.ok(likesByUserId);
    }

}
