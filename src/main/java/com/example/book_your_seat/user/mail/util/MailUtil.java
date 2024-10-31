package com.example.book_your_seat.user.mail.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

import static com.example.book_your_seat.user.UserConst.MAIL_CREATION_FAILED;
import static com.example.book_your_seat.user.mail.MailConst.*;
import static com.example.book_your_seat.user.mail.MailConst.EMAIL_TEMPLATE;

@Component
@RequiredArgsConstructor
public class MailUtil {
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    @Async
    public CompletableFuture<String> sendMail(String to) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF8);
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
