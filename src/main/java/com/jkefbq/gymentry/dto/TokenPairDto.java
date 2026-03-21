package com.jkefbq.gymentry.dto;

import lombok.Data;

@Data
public class TokenPairDto {
    private String accessToken;
    private String refreshToken;
}
