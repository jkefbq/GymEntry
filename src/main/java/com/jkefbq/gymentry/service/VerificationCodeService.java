package com.jkefbq.gymentry.service;

import com.jkefbq.gymentry.exception.TimeoutActivationCodeException;

public interface VerificationCodeService {
    String generateAndSaveVerificationCode(String email);
    boolean compareVerificationCode(String email, String code) throws TimeoutActivationCodeException;
    void deleteVerificationCode(String email);
}
