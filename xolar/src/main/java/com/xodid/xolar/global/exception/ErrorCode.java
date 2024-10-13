package com.xodid.xolar.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PANEL_NOT_FOUND(HttpStatus.NOT_FOUND, "PANEL-001","해당 태양광 패널을 찾을 수 없습니다."),
    ELECTRONIC_NOT_FOUND(HttpStatus.NOT_FOUND, "ELEC-001","해당 태양광 패널의 전력 정보를 찾을 수 없습니다."),
    BILL_NOT_FOUNT(HttpStatus.NOT_FOUND, "BILL-001","해당 태양광 패널의 요금 정보를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus; //HttpStatus
    private final String code; // ex) ACCOUNT-001
    private final String message; // 설명
}
