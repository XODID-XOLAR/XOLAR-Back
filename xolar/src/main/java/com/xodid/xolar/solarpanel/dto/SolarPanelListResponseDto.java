package com.xodid.xolar.solarpanel.dto;

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

    public static SolarPanelListResponseDto from(SolarPanel solarPanel, Integer todayElecGeneration){
        return new SolarPanelListResponseDto(
                solarPanel.getPanelId(),
                solarPanel.getArea(),
                todayElecGeneration,
                solarPanel.getOperationStatus()
        );
    }
}
