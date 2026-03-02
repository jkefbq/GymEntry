package com.jkefbq.gymentry.security;

public interface JwtService {
    TokenPairDto generateTokenPair(String email);
    TokenPairDto refreshAccessTokenAndRotate(String email);
    boolean isAnyTokenValid(String refreshToken);
    String getEmailFromAccessToken(String token);

    String generateAccessToken(String email);

    String generateRefreshToken(String email);
}
