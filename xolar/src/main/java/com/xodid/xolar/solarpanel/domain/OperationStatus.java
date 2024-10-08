package com.xodid.xolar.solarpanel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperationStatus {
    NORMAL(0,"정상 작동"),
    DISCONNECT(1,"연결 해제"),
    INOPERABLE(2,"작동 불가");

    private final Integer id;
    private final String title;
}
