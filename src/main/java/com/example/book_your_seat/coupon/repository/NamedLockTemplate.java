package com.example.book_your_seat.coupon.repository;

import static java.util.Objects.isNull;

import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * JDBC 를 활용해서 네임드 락을 구현하는 방법입니다.
 */

@Component
@RequiredArgsConstructor
public class NamedLockTemplate {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String GET_LOCK = "SELECT GET_LOCK(:userLockName, :timeoutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:userLockName)";

    // 여기서 트랜잭션 전파 단계를 REQUIRES_NEW 로 설정해야 락 획득/반납과 로직 처리가 분리되어서
    // 로직처리의 원자성을 보장할 수 있습니다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T executeWithLock(String userLockName, int timeoutSeconds, Supplier<T> supplier) {
        try {
            getLock(userLockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(userLockName);
        }
    }

    private void getLock(String userLockName, int timeoutSeconds) {
        Map<String, Object> params = Map.of(
                "userLockName", userLockName,
                "timeoutSeconds", timeoutSeconds
        );

        Integer result = namedParameterJdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
        checkResult(result);
    }

    private void releaseLock(String userLockName) {
        Map<String, Object> params = Map.of("userLockName", userLockName);

        Integer result = namedParameterJdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
        checkResult(result);
    }

    private void checkResult(Integer result) {
        if (isNull(result)) {
            throw new IllegalArgumentException("locked.resource1");
        }
    }
}
