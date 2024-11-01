package com.example.book_your_seat.likeconcert.service.command;

import com.example.book_your_seat.likeconcert.domain.LikeConcert;
import com.example.book_your_seat.likeconcert.repository.LikeConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeConcertCommandService {


    private final LikeConcertRepository likeConcertRepository;

    public void like(Long userId, Long concertId) {
        LikeConcert likeConcert = new LikeConcert(userId, concertId);
        boolean isAlreadyExists = likeConcertRepository.existsByUserIdAndConcertId(userId, concertId);

        if (isAlreadyExists) {
            likeConcertRepository.deleteByUserIdAndConcertId(userId, concertId);
        }
        likeConcertRepository.save(likeConcert);
    }
}
