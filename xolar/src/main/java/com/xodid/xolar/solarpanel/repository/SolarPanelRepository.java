package com.xodid.xolar.solarpanel.repository;

import com.xodid.xolar.solarpanel.domain.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long> {
}
