package com.example.book_your_seat.user;

public final class UserConst {

    private UserConst() {}
    public static final String INVALID_LOGIN_REQUEST = "이메일 또는 비밀번호가 올바르지 않습니다.";
    public static final String INVALID_EMAIL = "가입되지 않은 이메일입니다.";
    public static final String ADDRESS_NOT_FOUND = "해당 주소를 찾을 수 없습니다.";
    public static final String ALREADY_JOIN_EMAIL = "이미 가입한 이메일입니다.";
    public static final String ADDRESS_NOT_OWNED = "로그인한 유저의 주소가 아닙니다.";
    public static final String MAIL_CREATION_FAILED = "메일을 생성할 수 없습니다.";
    public static final String INVALID_CERT_CODE = "인증 코드가 올바르지 않습니다.";
    public static final String EMAIL_NOT_VERIFIED = "인증되지 않은 이메일입니다.";
    public static final String USER_NOT_FOUND = "해당 유저를 찾을 수 없습니다.";

    public static final String EMAIL_CERT_CODE_KEY = "emailCertCode:";
    public static final String VERIFIED_EMAIL_KEY = "email:";

}
