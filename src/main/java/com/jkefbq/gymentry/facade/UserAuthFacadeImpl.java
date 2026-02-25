package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.NotVerifiedUserDto;
import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.service.NotVerifiedUserService;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.exception.InvalidTokenException;
import com.jkefbq.gymentry.exception.InvalidVerificationCodeException;
import com.jkefbq.gymentry.exception.TimeoutActivationCodeException;
import com.jkefbq.gymentry.exception.UserAlreadyExistsException;
import com.jkefbq.gymentry.security.JwtService;
import com.jkefbq.gymentry.security.TokenPairDto;
import com.jkefbq.gymentry.security.UserCredentialsDto;
import com.jkefbq.gymentry.security.UserRole;
import com.jkefbq.gymentry.service.MailService;
import com.jkefbq.gymentry.service.VerificationCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class UserAuthFacadeImpl implements UserAuthFacade {

    private static final ExecutorService es = Executors.newCachedThreadPool();
    private final NotVerifiedUserService notVerifiedUserService;
    private final UserService userService;
    private final MailService mailService;
    private final JwtService jwtService;
    private final VerificationCodeService verificationCodeService;

    @Override
    @Transactional
    public void register(NotVerifiedUserDto user) throws UserAlreadyExistsException {
        boolean existsTmp = notVerifiedUserService.existsByEmail(user.getEmail());
        boolean existsCommon = userService.existsByEmail(user.getEmail());
        if (existsTmp || existsCommon) {
            throw new UserAlreadyExistsException("user with email '" + user.getEmail() + "' already exists");
        }
        notVerifiedUserService.create(user);
        es.execute(() -> mailService.sendConfirmEmail(user.getEmail()));
    }

    @Override
    @Transactional
    public TokenPairDto login(UserCredentialsDto userCredentials) throws AuthenticationException {
        boolean isCorrectData = userService.isCorrectEmailAndPassword(userCredentials.getEmail(), userCredentials.getPassword());
        if (!isCorrectData) {
            throw new AuthenticationException("incorrect login or password");
        }
        return jwtService.generateTokenPair(userCredentials.getEmail());
    }

    @Override
    @Transactional
    public TokenPairDto activate(String email, String code) throws InvalidVerificationCodeException, TimeoutActivationCodeException {
        if (!verificationCodeService.compareVerificationCode(email, code)) {
            throw new InvalidVerificationCodeException("incorrect verification code");
        }
        notVerifiedUserService.findUserByEmail(email).ifPresentOrElse(
                this::deleteTmpUserAndCreateCommonUser,
                () -> {
                    throw new IllegalStateException("non-verified user with email '" + email + "' not found");
                });
        return jwtService.generateTokenPair(email);
    }

    @Override
    @Transactional
    public TokenPairDto refresh(String refreshToken) throws InvalidTokenException {
        if (jwtService.isAnyTokenValid(refreshToken)) {
            String email = jwtService.getEmailFromAccessToken(refreshToken);
            UserDto authenticUser = userService.findByEmail(email)
                    .orElseThrow(() -> new NoSuchElementException("no user with email " + email));
            return jwtService.refreshAccessTokenAndRotate(authenticUser.getEmail());
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    @Override
    public String resendActivationCode(String email) {
        verificationCodeService.deleteVerificationCode(email);
        return verificationCodeService.generateAndSaveVerificationCode(email);
    }

    @Transactional
    public void deleteTmpUserAndCreateCommonUser(NotVerifiedUserDto notVerifiedUser) {
        var verifiedUser = UserDto.builder()
                .firstName(notVerifiedUser.getFirstName())
                .password(notVerifiedUser.getPassword())
                .email(notVerifiedUser.getEmail())
                .role(UserRole.USER)
                .build();
        userService.create(verifiedUser);
        notVerifiedUserService.deleteByEmail(notVerifiedUser.getEmail());
    }
}
