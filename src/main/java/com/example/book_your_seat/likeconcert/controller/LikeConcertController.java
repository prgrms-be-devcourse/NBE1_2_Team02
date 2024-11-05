package com.example.book_your_seat.likeconcert.controller;

import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.config.security.auth.LoginUser;
import com.example.book_your_seat.likeconcert.controller.dto.AddLikeRequest;
import com.example.book_your_seat.likeconcert.service.facade.LikeConcertFacade;
import com.example.book_your_seat.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/likes")
public class LikeConcertController {

    private final LikeConcertFacade likeConcertFacade;

    @Operation(
            summary = "관심 공연 목록에 공연을 추가합니다.",
            description = "관심 공연 목록에 공연을 추가합니다."
    )
    @PostMapping
    public ResponseEntity<Void> like(
            @LoginUser User user,
            @RequestBody AddLikeRequest request
    ) {
        likeConcertFacade.addLike(user.getId(), request.concertId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "모든 관심 공연 목록을 조회합니다.",
            description = "모든 관심 공연 목록을 조회합니다."
    )
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
