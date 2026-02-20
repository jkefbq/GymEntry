package com.jkefbq.gymentry.security;

import lombok.Data;

@Data
public class TokenPairDto {
    private String accessToken;
    private String refreshToken;
}
