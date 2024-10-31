package com.example.book_your_seat.likeconcert.service.command;

import com.example.book_your_seat.likeconcert.domain.LikeConcert;
import com.example.book_your_seat.likeconcert.repository.LikeConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.book_your_seat.likeconcert.LikeConst.DUPLICATE_LIKE;
import static com.example.book_your_seat.likeconcert.LikeConst.INVALID_LIKE;

@Service
@RequiredArgsConstructor
public class LikeConcertCommandService {


    private final LikeConcertRepository likeConcertRepository;

    public Long like(Long userId, Long concertId) {
        LikeConcert likeConcert = new LikeConcert(userId, concertId);
        validateDuplicate(userId, concertId);

        return likeConcertRepository.save(likeConcert).getId();
    }

    private void validateDuplicate(Long userId, Long concertId) {
        boolean isAlreadyExists = likeConcertRepository.existsByUserIdAndConcertId(userId, concertId);

        if (isAlreadyExists) {
            throw new IllegalArgumentException(DUPLICATE_LIKE);
        }
    }

    public void delete(Long likeConcertId) {
        validateExists(likeConcertId);
        likeConcertRepository.deleteById(likeConcertId);
    }

    private void validateExists(Long likeConcertId) {
        boolean isNotExists = likeConcertRepository.existsById(likeConcertId);

        if (isNotExists) {
            throw new IllegalArgumentException(INVALID_LIKE);
        }
    }
}
