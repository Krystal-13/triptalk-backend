package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원이 존재하지 않습니다."),
    EMAIL_APPROVAL_DENIED(HttpStatus.BAD_REQUEST, "일치하는 회원이 존재하지 않습니다."),
    PASSWORD_APPROVAL_DENIED(HttpStatus.BAD_REQUEST, "비밀번호는 특수문자,영어,숫자를 포함해야 하며 , 8글자 이상이여야 합니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일 입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임 입니다."),
    KAKAO_NICKNAME_ERROR(HttpStatus.BAD_REQUEST, "카카오 로그인을 다시 시도해 주세요!"),
    EMAIL_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "이메일을 찾을 수 없습니다!"),
    NO_VAILD_EMAIL_AND_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호의 정보가 일치하지 않습니다. 다시 확인해주세요"),
    EMAIL_CHECK_FAIL_NO_SAME_ERROR(HttpStatus.BAD_REQUEST, "이메일이 동일하지 않습니다!"),
    PASSWORD_NOT_SAME(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "지원하지 않는 로그인입니다.");


    private final HttpStatus status;
    private final String errorMessage;
}
