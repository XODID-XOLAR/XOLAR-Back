package com.xodid.xolar.electronic.domain;

import com.xodid.xolar.solarpanel.domain.SolarPanel;
import com.xodid.xolar.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert // insert시 지정된 default값 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Electronic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="elec_id",updatable = false)
    private Long elecId;

    @Column(name = "elec_generation", nullable = false)
    @ColumnDefault("0")
    private Integer elecGeneration;

    @Column(name = "elec_consumption", nullable = false)
    @ColumnDefault("0")
    private Integer elecConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panel_id", updatable = false)
    private SolarPanel solarPanel;
}
