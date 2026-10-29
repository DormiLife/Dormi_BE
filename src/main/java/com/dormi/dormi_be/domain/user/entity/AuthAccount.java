package com.dormi.dormi_be.domain.user.entity;

import com.dormi.dormi_be.domain.user.enums.AuthProvider;
import com.dormi.dormi_be.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "auth_accounts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_auth_accounts_provider_user_id",
                columnNames = {"provider", "provider_user_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    @Column(nullable = false)
    private String providerUserId;

    private String passwordHash;

    @Builder
    private AuthAccount(User user, AuthProvider provider, String providerUserId, String passwordHash) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.passwordHash = passwordHash;
    }
}
