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
    public ResponseEntity<Void> like(@LoginUser User user,
                                     @RequestBody AddLikeRequest request
    ) {
        likeConcertFacade.addLike(user.getId(), request.concertId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ConcertListResponse>> findAll(@LoginUser User user) {
        List<ConcertListResponse> likesByUserId = likeConcertFacade.findLikesByUserId(user.getId());
        return ResponseEntity.ok(likesByUserId);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(DeleteLikeRequest request
    ) {
        likeConcertFacade.deleteLike(request.likeConcertId());
        return ResponseEntity.noContent().build();
    }
}
