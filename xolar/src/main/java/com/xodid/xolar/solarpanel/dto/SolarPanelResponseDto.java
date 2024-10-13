package com.xodid.xolar.solarpanel.dto;

import com.xodid.xolar.bill.domain.Bill;
import com.xodid.xolar.electronic.domain.Electronic;
import com.xodid.xolar.solarpanel.domain.OperationStatus;
import com.xodid.xolar.solarpanel.domain.SolarPanel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolarPanelResponseDto {
    private Long panelId;
    private String area;
    private Integer imageNumber;
    private OperationStatus operationStatus;
    private Integer elecGeneration;
    private Integer elecConsumption;
    private Integer billGeneration;
    private Integer billConsumption;

    public static SolarPanelResponseDto from(SolarPanel solarPanel, Electronic electronic, Bill bill) {
        return new SolarPanelResponseDto(
                solarPanel.getPanelId(),
                solarPanel.getArea(),
                solarPanel.getImageNumber(),
                solarPanel.getOperationStatus(),
                electronic.getElecGeneration(),
                electronic.getElecConsumption(),
                bill.getBillGeneration(),
                bill.getBillConsumption()
        );

    }
}
