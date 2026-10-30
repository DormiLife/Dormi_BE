package com.dormi.dormi_be.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON400", "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401_1", "인증이 필요합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH401_2", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH401_3", "유효하지 않은 리프레시 토큰입니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.CONFLICT, "USER409_1", "이미 가입된 계정이 존재합니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "USER409_2", "이미 사용 중인 별명입니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "USER409_3", "이미 사용 중인 이메일입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
