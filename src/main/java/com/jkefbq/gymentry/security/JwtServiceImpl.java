package com.jkefbq.gymentry.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private static final TemporalAmount ACCESS_TTL = Duration.ofHours(1);
    private static final TemporalAmount REFRESH_TTL = Duration.ofDays(18);

    @Value("${app.auth.encryption-key}")
    private String jwtSecretEncryptionKey;

    @Override
    public TokenPairDto generateTokenPair(String email) {
        TokenPairDto jwtDto = new TokenPairDto();
        jwtDto.setAccessToken(generateAccessToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return jwtDto;
    }

    @Override
    public TokenPairDto refreshAccessTokenAndRotate(String email) {
        TokenPairDto jwtDto = new TokenPairDto();
        jwtDto.setAccessToken(generateAccessToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return jwtDto;
    }

    @Override
    public boolean isAnyTokenValid(String refreshToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SecurityException e) {
            log.error("exception while validating token: ", e);
            throw e;
        }
    }

    @Override
    public String getEmailFromAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    private String generateAccessToken(String email) {
        Date date = Date.from(LocalDateTime.now().plus(ACCESS_TTL).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSecretKey())
                .compact();
    }

    private String generateRefreshToken(String email) {
        Date date = Date.from(LocalDateTime.now().plus(REFRESH_TTL).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretEncryptionKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
