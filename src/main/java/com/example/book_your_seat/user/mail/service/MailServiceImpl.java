package com.example.book_your_seat.user.mail.service;

import com.example.book_your_seat.user.mail.repository.MailRedisRepository;
import com.example.book_your_seat.user.mail.repository.util.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.book_your_seat.user.UserConst.EMAIL_NOT_VERIFIED;
import static com.example.book_your_seat.user.UserConst.INVALID_CERT_CODE;
import static com.example.book_your_seat.user.mail.MailConst.MAIL_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailUtil mailUtil;
    private final MailRedisRepository mailRedisRepository;

    @Override
    public Boolean sendCertMail(String email) {
        mailUtil.sendMail(email).thenAccept(certCord -> {
            mailRedisRepository.saveEmailCertCode(email, certCord);
            log.info("{} {}", MAIL_SUCCESS, email);
        });
        return true;
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

}
