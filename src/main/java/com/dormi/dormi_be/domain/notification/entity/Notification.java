package com.dormi.dormi_be.domain.notification.entity;

import com.dormi.dormi_be.domain.notification.enums.NotificationType;
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
        name = "notifications",
        indexes = @Index(name = "idx_notifications_user_id_id", columnList = "user_id, id DESC")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(length = 30)
    private String relatedType;

    private Long relatedId;

    private LocalDateTime readAt;

    @Builder
    private Notification(User user, NotificationType type, String title, String content,
                         String relatedType, Long relatedId) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.content = content;
        this.relatedType = relatedType;
        this.relatedId = relatedId;
    }

    public void markRead(LocalDateTime now) {
        if (this.readAt == null) {
            this.readAt = now;
        }
    }
}
