package com.xodid.xolar.solarpanel.dto;

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

    public static SolarPanelResponseDto from(SolarPanel solarPanel, int elecGeneration, int elecConsumption, int billGeneration, int billConsumption) {
        return new SolarPanelResponseDto(
                solarPanel.getPanelId(),
                solarPanel.getArea(),
                solarPanel.getImageNumber(),
                solarPanel.getOperationStatus(),
                elecGeneration,
                elecConsumption,
                billGeneration,
                billConsumption
        );

    }
}
