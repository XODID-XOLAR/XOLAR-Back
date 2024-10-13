package com.xodid.xolar.bill.service;

import com.xodid.xolar.bill.domain.Bill;
import com.xodid.xolar.bill.repository.BillRepository;
import com.xodid.xolar.solarpanel.domain.SolarPanel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;

    /* Transaction 함수들 */

    // 해당 태양광패널의 모든 bill 리스트 조회
    @Transactional(readOnly = true)
    public List<Bill> findAllBySolarPanel(SolarPanel solarPanel){
        return billRepository.findAllBySolarPanel(solarPanel);
    }
}
