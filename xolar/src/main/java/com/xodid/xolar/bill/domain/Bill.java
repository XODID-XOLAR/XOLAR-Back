package com.xodid.xolar.bill.domain;

import com.xodid.xolar.solarpanel.domain.SolarPanel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert // insert시 지정된 default값 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bill_id",updatable = false)
    private Long billId;

    @Column(name = "bill_generation", nullable = false)
    @ColumnDefault("0")
    private Integer billGeneration;

    @Column(name = "bill_consumption", nullable = false)
    @ColumnDefault("0")
    private Integer billConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panel_id", updatable = false)
    private SolarPanel solarPanel;
}
