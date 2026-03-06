package com.jkefbq.gymentry.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntryCodeServiceImplTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    RedisTemplate<String, Object> redisTemplate;
    ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);

    @InjectMocks
    EntryCodeServiceImpl entryCodeService;

    @Test
    public void generateUserEntryCodeTest() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        var email = "email";
        var response = entryCodeService.generateUserEntryCode(email);

        verify(valueOperations).set(any(), eq(email), any());
        assertNotNull(response);
    }

    @Test
    public void getEmailByCodeTest() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        var code = "123456";
        entryCodeService.getEmailByCode(code);

        verify(valueOperations).get(any());
    }
}
