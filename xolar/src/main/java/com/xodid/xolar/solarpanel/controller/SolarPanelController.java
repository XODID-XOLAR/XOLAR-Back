package com.xodid.xolar.solarpanel.controller;

import com.xodid.xolar.solarpanel.dto.SolarPanelListResponseDto;
import com.xodid.xolar.solarpanel.dto.SolarPanelResponseDto;
import com.xodid.xolar.solarpanel.service.SolarPanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/solar-panels")
public class SolarPanelController {
    private final SolarPanelService solarPanelService;

    // 태양광 패널 상세 조회
    @GetMapping("{panel_id}")
    public ResponseEntity<SolarPanelResponseDto> getSolarPanel(@PathVariable Long panel_id){
        return ResponseEntity.status(HttpStatus.OK).body(solarPanelService.findSolarPanelById(panel_id));
    }

    // 태양광 패널 목록 조회
    @GetMapping
    public ResponseEntity<List<SolarPanelListResponseDto>> getAllSolarPanels(){
        return ResponseEntity.status(HttpStatus.OK).body(solarPanelService.findAllSolarPanels());
    }
}
