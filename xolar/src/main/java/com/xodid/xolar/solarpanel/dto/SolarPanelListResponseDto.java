package com.xodid.xolar.solarpanel.dto;

import com.xodid.xolar.electronic.domain.Electronic;
import com.xodid.xolar.solarpanel.domain.OperationStatus;
import com.xodid.xolar.solarpanel.domain.SolarPanel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SolarPanelListResponseDto {
    private Long panelId;
    private String area;
    private Integer elecGeneration;
    private OperationStatus operationStatus;

    public static SolarPanelListResponseDto from(SolarPanel solarPanel, Electronic electronic){
        return new SolarPanelListResponseDto(
                solarPanel.getPanelId(),
                solarPanel.getArea(),
                electronic.getElecGeneration(),
                solarPanel.getOperationStatus()
        );
    }
}
