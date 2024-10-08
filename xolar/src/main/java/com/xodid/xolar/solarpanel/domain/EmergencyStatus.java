package com.xodid.xolar.solarpanel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmergencyStatus {
    NORMAL(0,"비상해제"),
    STRONG_WIND(1,"강풍"),
    HEAVY_SNOW(2,"폭설");

    private final Integer id;
    private final String title;
}
