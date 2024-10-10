package com.example.book_your_seat.user.mail.service;

public interface MailService {

    Boolean sendCertMail(String email);

    Boolean checkCertCode(String email, String certCode);

    void checkVerifiedEmail(String email);
}
