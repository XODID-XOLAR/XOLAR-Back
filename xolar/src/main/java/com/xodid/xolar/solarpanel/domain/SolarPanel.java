package com.xodid.xolar.solarpanel.domain;

import com.xodid.xolar.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class SolarPanel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="panel_id",updatable = false)
    private Long panelId;

    @Column(nullable = false)
    private String area;

    @Column(name = "panel_number", nullable = false)
    private String panelNumber;

    @Column(name = "image_number", nullable = false)
    @ColumnDefault("3")
    private Integer imageNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "emergency_status", nullable = false)
    @ColumnDefault("'NORMAL'")
    private EmergencyStatus emergencyStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status", nullable = false)
    @ColumnDefault("'NORMAL'")
    private OperationStatus operationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;
}
