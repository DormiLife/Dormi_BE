package com.dormi.dormi_be.domain.facility.entity;

import com.dormi.dormi_be.domain.facility.enums.ReservationStatus;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "meeting_reservations",
        indexes = @Index(name = "idx_meeting_reservations_user_id_start_at", columnList = "user_id, start_at DESC")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingReservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    private LocalDateTime cancelledAt;

    @Builder
    private MeetingReservation(User user, LocalDateTime startAt, LocalDateTime endAt) {
        this.user = user;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = ReservationStatus.RESERVED;
    }

    public void cancel(LocalDateTime now) {
        this.status = ReservationStatus.CANCELLED;
        this.cancelledAt = now;
    }
}
