package com.dormi.dormi_be.domain.facility.entity;

import com.dormi.dormi_be.domain.facility.enums.UsageStatus;
import com.dormi.dormi_be.domain.user.entity.User;
import com.dormi.dormi_be.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "machine_usages",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_machine_usages_active_machine_id",
                columnNames = "active_machine_id"
        ),
        indexes = {
                @Index(name = "idx_machine_usages_user_id_id", columnList = "user_id, id DESC"),
                @Index(name = "idx_machine_usages_status_expected_end_at", columnList = "status, expected_end_at")
        }
)
@Check(
        name = "chk_machine_usages_active_machine_id",
        constraints = "(status = 'IN_USE') = (active_machine_id IS NOT NULL)"
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MachineUsage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private FacilityMachine machine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UsageStatus status;

    private Long activeMachineId;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime expectedEndAt;

    private LocalDateTime notifiedAt;

    private LocalDateTime pickedUpAt;

    private LocalDateTime cancelledAt;

    @Builder
    private MachineUsage(FacilityMachine machine, User user, LocalDateTime startedAt, LocalDateTime expectedEndAt) {
        this.machine = machine;
        this.user = user;
        this.startedAt = startedAt;
        this.expectedEndAt = expectedEndAt;
        this.status = UsageStatus.IN_USE;
        this.activeMachineId = machine.getId();
    }

    public void markNotified(LocalDateTime now) {
        this.notifiedAt = now;
    }

    public void pickUp(LocalDateTime now) {
        this.status = UsageStatus.PICKED_UP;
        this.pickedUpAt = now;
        this.activeMachineId = null;
    }

    public void cancel(LocalDateTime now) {
        this.status = UsageStatus.CANCELLED;
        this.cancelledAt = now;
        this.activeMachineId = null;
    }
}
