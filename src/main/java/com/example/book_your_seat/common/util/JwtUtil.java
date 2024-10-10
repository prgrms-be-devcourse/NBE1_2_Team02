package com.example.book_your_seat.common.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static com.example.book_your_seat.common.util.JwtConst.*;

@Slf4j
@Component
public abstract class JwtUtil {
    protected final SecretKey secretKey;
    private static final String SIGNATURE_ALGORITHM = Jwts.SIG.HS256.key().build().getAlgorithm();

    protected JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new IllegalStateException(INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException(EXPIRED_JWT);
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new IllegalStateException(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(EMPTY_JWT);
        }
    }

}
