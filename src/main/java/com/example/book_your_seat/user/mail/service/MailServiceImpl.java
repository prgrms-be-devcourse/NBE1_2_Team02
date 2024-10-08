package com.example.book_your_seat.user.mail.service;

import com.example.book_your_seat.user.mail.repository.MailRedisRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

import static com.example.book_your_seat.user.UserConst.*;
import static com.example.book_your_seat.user.mail.MailConst.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final MailRedisRepository mailRedisRepository;

    @Value("${spring.mail.username}")
    String from;

    @Override
    public Boolean sendCertMail(String email) {
        sendMail(email).thenAccept(certCord -> {
            mailRedisRepository.saveEmailCertCode(email, certCord);
            log.info("{} {}", MAIL_SUCCESS, email);
        });
        return true;
    }

    @Async
    public CompletableFuture<String> sendMail(String to) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(MAIL_SUBJECT);
            helper.setFrom(from);

            //데이터 설정
            String certCode = generateCertCode();
            Context context = new Context();
            context.setVariable(MAIL, to);
            context.setVariable(CERTCODE, certCode);

            //html 매핑
            String htmlContent = templateEngine.process(EMAIL_TEMPLATE, context);
            helper.setText(htmlContent, true);

            //메일 전송
            mailSender.send(message);
            return CompletableFuture.completedFuture(certCode);

        } catch (MessagingException e) {
            throw new IllegalStateException(MAIL_CREATION_FAILED);
        }
    }

    @Override
    public Boolean checkCertCode(String email, String certCode) {
        String savedCertCode = mailRedisRepository.findCertCodeByEmail(email);

        compareCertCode(certCode, savedCertCode);

        mailRedisRepository.saveVerifiedEmail(email);
        return true;
    }

    @Override
    public void checkVerifiedEmail(String email) {
        if (mailRedisRepository.findVerifiedEmail(email) == null)
            throw new IllegalArgumentException(EMAIL_NOT_VERIFIED);
    }

    private void compareCertCode(String certCode, String savedCertCode) {
        if (!savedCertCode.equals(certCode)) {
            throw new IllegalArgumentException(INVALID_CERT_CODE);
        }
    }

    private String generateCertCode() {
        final String candidateChars = NUMBER_AND_ALPHABET;
        final int certCodeLength = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder certCode = new StringBuilder(certCodeLength);

        for (int i = 0; i < certCodeLength; i++) {
            int index = random.nextInt(candidateChars.length());
            certCode.append(candidateChars.charAt(index));
        }
        return certCode.toString();
    }
}