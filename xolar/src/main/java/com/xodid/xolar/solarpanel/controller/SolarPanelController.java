package com.xodid.xolar.solarpanel.controller;

import com.xodid.xolar.global.exception.CustomException;
import com.xodid.xolar.global.exception.ErrorCode;
import com.xodid.xolar.solarpanel.dto.SolarPanelResponseDto;
import com.xodid.xolar.solarpanel.service.SolarPanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/solar-panels")
public class SolarPanelController {
    private final SolarPanelService solarPanelService;

    @GetMapping("{panel_id}")
    public ResponseEntity<SolarPanelResponseDto> getSolarPanel(@PathVariable Long panel_id){
        return ResponseEntity.status(HttpStatus.OK).body(solarPanelService.findSolarPanelById(panel_id));
    }
}
