package com.xodid.xolar.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PANEL_NOT_FOUND(HttpStatus.NOT_FOUND, "PANEL-001","해당 태양광 패널을 찾을 수 없습니다."),
    ELECTRONIC_NOT_FOUND(HttpStatus.NOT_FOUND, "ELEC-001","해당 태양광 패널의 전력 정보를 찾을 수 없습니다."),
    BILL_NOT_FOUNT(HttpStatus.NOT_FOUND, "BILL-001","해당 태양광 패널의 요금 정보를 찾을 수 없습니다."),

    // User 관련 에러 코드
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER-001","해당 사용자를 찾을 수 없습니다."),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER-002","해당 이메일의 유저가 존재하지 않습니다."),

    // JWT 관련 에러 코드
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-002", "만료된 토큰입니다.")
    ;

    private final HttpStatus httpStatus; //HttpStatus
    private final String code; // ex) ACCOUNT-001
    private final String message; // 설명
}
