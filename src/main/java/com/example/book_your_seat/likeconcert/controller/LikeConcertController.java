package com.example.book_your_seat.likeconcert.controller;

import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.likeconcert.controller.dto.AddLikeRequest;
import com.example.book_your_seat.likeconcert.controller.dto.DeleteLikeRequest;
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
    public ResponseEntity<Long> like(
            @LoginUser User user,
            @RequestBody AddLikeRequest request
    ) {
        Long likeId = likeConcertFacade.addLike(user.getId(), request.concertId());
        return ResponseEntity.ok(likeId);
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

    @DeleteMapping
    public ResponseEntity<Void> delete(DeleteLikeRequest request) {
        likeConcertFacade.deleteLike(request.likeConcertId());
        return ResponseEntity.noContent().build();
    }
}
