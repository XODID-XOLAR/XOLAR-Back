package com.xodid.xolar.solarpanel.dto;

import com.xodid.xolar.solarpanel.domain.SolarPanel;
import com.xodid.xolar.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolarPanelRequestDto {
    private String area;
    private String panelCode;

    public SolarPanel toEntity(User user){
        return SolarPanel.builder()
                .user(user)
                .area(this.area)
                .panelCode(this.panelCode)
                .build();
    }
}
