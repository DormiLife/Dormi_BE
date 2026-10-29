package com.dormi.dormi_be.domain.facility.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "meeting_reservation_slots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingReservationSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private MeetingReservation reservation;

    @Column(nullable = false, unique = true)
    private LocalDateTime slotStart;

    @Builder
    private MeetingReservationSlot(MeetingReservation reservation, LocalDateTime slotStart) {
        this.reservation = reservation;
        this.slotStart = slotStart;
    }
}
