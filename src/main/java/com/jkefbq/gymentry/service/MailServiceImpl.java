package com.jkefbq.gymentry.service;

import com.jkefbq.gymentry.props.MailProps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final String SUBJECT = "Mail confirmation";
    private static final String MESSAGE = "your verification code\n";
    private final JavaMailSender mailSender;
    private final MailProps mailProps;
    private final VerificationCodeService verificationCodeService;

    @SneakyThrows
    public void sendConfirmEmail(String emailTo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String verificationCode = verificationCodeService.generateAndSaveVerificationCode(emailTo);
        mailMessage.setText(MESSAGE + verificationCode);
        mailMessage.setFrom(mailProps.getUsername());
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(SUBJECT);
        mailSender.send(mailMessage);
    }
}
