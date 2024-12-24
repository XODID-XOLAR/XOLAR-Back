package com.xodid.xolar.solarpanel.repository;

import com.xodid.xolar.solarpanel.domain.SolarPanel;
import com.xodid.xolar.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long> {
    List<SolarPanel> findAllByUser(User user);
    Boolean existsByPanelCode(String panelCode);
}
