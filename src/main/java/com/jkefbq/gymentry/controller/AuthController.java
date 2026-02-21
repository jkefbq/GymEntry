package com.jkefbq.gymentry.controller;

import com.jkefbq.gymentry.database.dto.NotVerifiedUserDto;
import com.jkefbq.gymentry.exception.InvalidTokenException;
import com.jkefbq.gymentry.exception.InvalidVerificationCodeException;
import com.jkefbq.gymentry.exception.TimeoutActivationCodeException;
import com.jkefbq.gymentry.exception.UserAlreadyExistsException;
import com.jkefbq.gymentry.facade.UserAuthFacade;
import com.jkefbq.gymentry.security.TokenPairDto;
import com.jkefbq.gymentry.security.UserCredentialsDto;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthFacade userAuthFacade;

    @PostMapping("/register")
    public ResponseEntity<@NonNull String> register(@RequestBody @Valid NotVerifiedUserDto user) throws UserAlreadyExistsException {
        userAuthFacade.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("successful registration, confirmation code has been sent to your email");
    }

    @GetMapping("/activate/{email}/{code}")
    public ResponseEntity<@NonNull TokenPairDto> activate(@PathVariable String email, @PathVariable String code) throws TimeoutActivationCodeException, InvalidVerificationCodeException {
        TokenPairDto tokenPair = userAuthFacade.activate(email, code);
        return ResponseEntity.ok(tokenPair);
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull TokenPairDto> signIn(@RequestBody @Valid UserCredentialsDto userCredentials) throws AuthenticationException {
        TokenPairDto tokenPair = userAuthFacade.login(userCredentials);
        return ResponseEntity.ok(tokenPair);
    }

    @PostMapping("/refresh")
    public ResponseEntity<@NonNull TokenPairDto> refresh(@RequestBody String refreshTokenDto) throws InvalidTokenException {
        TokenPairDto newTokenPair = userAuthFacade.refresh(refreshTokenDto);
        return ResponseEntity.ok(newTokenPair);
    }

}
