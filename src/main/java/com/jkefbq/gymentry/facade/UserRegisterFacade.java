package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.NotVerifiedUserDto;
import com.jkefbq.gymentry.exception.InvalidTokenException;
import com.jkefbq.gymentry.exception.InvalidVerificationCodeException;
import com.jkefbq.gymentry.exception.TimeoutActivationCodeException;
import com.jkefbq.gymentry.exception.UserAlreadyExistsException;
import com.jkefbq.gymentry.security.TokenPairDto;
import com.jkefbq.gymentry.security.UserCredentialsDto;

import javax.naming.AuthenticationException;

public interface UserRegisterFacade {
    void register(NotVerifiedUserDto user) throws UserAlreadyExistsException;
    TokenPairDto login(UserCredentialsDto userCredentials) throws AuthenticationException;
    TokenPairDto activate(String email, String code) throws InvalidVerificationCodeException, TimeoutActivationCodeException;
    TokenPairDto refresh(String refreshToken) throws InvalidTokenException;
}
