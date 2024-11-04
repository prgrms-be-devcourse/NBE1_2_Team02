package com.example.book_your_seat.seat.redis;

import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.domain.SeatId;
import com.example.book_your_seat.seat.repository.SeatRepository;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisExpiredListener extends KeyExpirationEventMessageListener {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    private final SeatRedisService seatRedisService;
    public RedisExpiredListener(RedisMessageListenerContainer listenerContainer, SeatRepository seatRepository, ReservationRepository reservationRepository, SeatRedisService seatRedisService) {
        super(listenerContainer);
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
        this.seatRedisService = seatRedisService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("seat:")) {
            String[] parts = expiredKey.split("[:\\-]");
            Long concertId = Long.valueOf(parts[1]);
            Long seatNumber = Long.valueOf(parts[2]);


            Seat seat = seatRepository.findByConcertIdAndSeatNo(concertId, seatNumber)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid seatId: " + concertId + "-" + seatNumber));

            validateSeatSold(seat);
            seatRedisService.deleteCache(seat.getId());
        }
    }

    private void validateSeatSold(Seat seat) {
        SeatId seatId = seat.getId();
        boolean hasNotPaid = !reservationRepository.existsBySeatId(seatId.getConcertId(), seatId.getSeatNumber());
        if (seat.isSold() && hasNotPaid) {
            seat.releaseSeat();
            seatRepository.save(seat);
        }
    }

}
