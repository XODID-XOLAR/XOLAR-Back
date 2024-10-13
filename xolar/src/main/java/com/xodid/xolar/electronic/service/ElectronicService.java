package com.xodid.xolar.electronic.service;

import com.xodid.xolar.electronic.domain.Electronic;
import com.xodid.xolar.electronic.repository.ElectronicRepository;
import com.xodid.xolar.solarpanel.domain.SolarPanel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectronicService {
    private final ElectronicRepository electronicRepository;

    /* Transaction 함수들 */

    // solar-panel로 electronic 조회
    @Transactional(readOnly = true)
    public List<Electronic> findAllBySolarPanel(SolarPanel solarPanel) {
        // 해당 태양광패널의 모든 electronic 리스트 조회
        return electronicRepository.findAllBySolarPanel(solarPanel);
    }
}
