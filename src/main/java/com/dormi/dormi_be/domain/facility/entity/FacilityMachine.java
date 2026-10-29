package com.dormi.dormi_be.domain.facility.entity;

import com.dormi.dormi_be.domain.facility.enums.MachineType;
import com.dormi.dormi_be.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "facility_machines",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_facility_machines_type_number",
                columnNames = {"machine_type", "machine_number"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityMachine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MachineType machineType;

    @Column(nullable = false)
    private int machineNumber;

    @Column(nullable = false)
    private boolean isActive;

    @Builder
    private FacilityMachine(MachineType machineType, int machineNumber) {
        this.machineType = machineType;
        this.machineNumber = machineNumber;
        this.isActive = true;
    }
}
