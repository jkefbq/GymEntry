package com.jkefbq.gymentry.service;

import com.jkefbq.gymentry.props.MailProps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    private static final String CODE = "123456";

    @Mock
    JavaMailSender mailSender;
    @Mock
    MailProps mailProps;
    @Mock
    VerificationCodeService verificationCodeService;

    @InjectMocks
    MailServiceImpl mailService;

    @Test
    public void sendConfirmEmailTest() {
        when(verificationCodeService.generateAndSaveVerificationCode(any())).thenReturn(CODE);

        mailService.sendConfirmEmail("email");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
