package com.xodid.xolar.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다.")

    ;

    private final HttpStatus httpStatus; //HttpStatus
    private final String code; // ex) ACCOUNT-001
    private final String message; // 설명
}
