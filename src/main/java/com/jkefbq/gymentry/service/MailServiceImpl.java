package com.jkefbq.gymentry.service;

import com.jkefbq.gymentry.props.MailProps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final String SUBJECT = "Mail confirmation";
    private static final String MESSAGE = "Follow the link to confirm your email\n";
    private static String ACTIVATE_URL;
    private final JavaMailSender mailSender;
    private final MailProps mailProps;
    private final VerificationCodeService verificationCodeService;

    @SneakyThrows
    public void sendConfirmEmail(String emailTo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String verificationCode = verificationCodeService.generateAndSaveVerificationCode(emailTo);
        mailMessage.setText(MESSAGE + ACTIVATE_URL.formatted(emailTo, verificationCode));
        mailMessage.setFrom(mailProps.getUsername());
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(SUBJECT);
        mailSender.send(mailMessage);
    }

    @Value("${app.controller.urls.activate}")
    public void setActivateUrl(String activateUrl) {
        ACTIVATE_URL = activateUrl;
    }
}
