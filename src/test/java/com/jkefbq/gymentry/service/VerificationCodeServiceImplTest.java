package com.jkefbq.gymentry.service;

import com.jkefbq.gymentry.exception.TimeoutActivationCodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationCodeServiceImplTest {

    private static final String EMAIL = "email@gmail.com";

    @Mock
    RedisTemplate<String, Object> redisTemplate;
    ValueOperations<String, Object> valOps = mock(ValueOperations.class);

    @InjectMocks
    VerificationCodeServiceImpl codeService;

    @Test
    public void generateAndSaveVerificationCodeTest() {
        when(redisTemplate.opsForValue()).thenReturn(valOps);

        var code = codeService.generateAndSaveVerificationCode(EMAIL);

        verify(valOps).set(any(), eq(code));
    }

    @Test
    public void compareVerificationCodeTest_assertFalse() throws TimeoutActivationCodeException {
        var codeToCompare = "169872";
        var trueCode = "999999";
        when(redisTemplate.opsForValue()).thenReturn(valOps);
        when(valOps.get(any())).thenReturn(trueCode);

        var isCorrect = codeService.compareVerificationCode(EMAIL, codeToCompare);

        assertFalse(isCorrect);
    }

    @Test
    public void compareVerificationCodeTest_assertTrue() throws TimeoutActivationCodeException {
        var codeToCompare = "169872";
        when(redisTemplate.opsForValue()).thenReturn(valOps);
        when(valOps.get(any())).thenReturn(codeToCompare);

        var isCorrect = codeService.compareVerificationCode(EMAIL, codeToCompare);

        assertTrue(isCorrect);
    }

    @Test
    public void deleteVerificationCodeTest() {
        codeService.deleteVerificationCode(EMAIL);

        verify(redisTemplate).delete(anyString());
    }
}