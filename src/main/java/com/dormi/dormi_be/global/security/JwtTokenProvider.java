package com.dormi.dormi_be.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String TOKEN_TYPE_CLAIM = "type";

    private final SecretKey key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String createAccessToken(Long userId) {
        return createToken(userId, TokenType.ACCESS, accessTokenExpirationMs);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, TokenType.REFRESH, refreshTokenExpirationMs);
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }

    /** 서명, 만료, 토큰 타입을 모두 검증하고 userId를 반환한다. 유효하지 않으면 JwtException. */
    public Long parseUserId(String token, TokenType expectedType) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (!expectedType.name().equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
            throw new JwtException("토큰 타입이 올바르지 않습니다.");
        }
        return Long.valueOf(claims.getSubject());
    }

    private String createToken(Long userId, TokenType type, long expirationMs) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim(TOKEN_TYPE_CLAIM, type.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }
}
