package com.xodid.xolar.bill.repository;

import com.xodid.xolar.bill.domain.Bill;
import com.xodid.xolar.solarpanel.domain.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllBySolarPanel(SolarPanel solarPanel);
}
