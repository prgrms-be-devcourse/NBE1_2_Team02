package com.example.book_your_seat.seat.redis;

import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisExpiredListener extends KeyExpirationEventMessageListener {

    private final SeatRepository seatRepository;
    private final SeatRedisService seatRedisService;
    public RedisExpiredListener(RedisMessageListenerContainer listenerContainer, SeatRepository seatRepository, SeatRedisService seatRedisService) {
        super(listenerContainer);
        this.seatRepository = seatRepository;
        this.seatRedisService = seatRedisService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("seat:")) {
            Long seatId = Long.valueOf(expiredKey.split(":")[1]);
            Seat seat = seatRepository.findById(seatId).orElseThrow(
                    () -> new IllegalArgumentException("Invalid seatId: " + seatId)
            );

            validateSeatSold(seat);
            seatRedisService.deleteCache(seat.getId());
        }
    }

    private void validateSeatSold(Seat seat) {
        if (seat.isSold() && seat.getReservation() == null) {
            seat.releaseSeat();
            seatRepository.save(seat);
        }
    }

}
