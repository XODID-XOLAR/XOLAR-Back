package com.xodid.xolar.electronic.repository;

import com.xodid.xolar.electronic.domain.Electronic;
import com.xodid.xolar.solarpanel.domain.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectronicRepository extends JpaRepository<Electronic, Long> {
    // 특정 SolarPanel과 연관된 Electronic 엔티티 리스트 반환
    List<Electronic> findAllBySolarPanel(SolarPanel solarPanel);
}
