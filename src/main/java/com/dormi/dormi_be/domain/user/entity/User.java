package com.dormi.dormi_be.domain.user.entity;

import com.dormi.dormi_be.domain.user.enums.Gender;
import com.dormi.dormi_be.domain.user.enums.UserStatus;
import com.dormi.dormi_be.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    public static final String WITHDRAWN_NICKNAME_PREFIX = "탈퇴한사용자_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 20)
    private String roomNumber;

    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(unique = true, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Builder
    private User(String name, String roomNumber, String nickname, Gender gender, String phoneNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.nickname = nickname;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.status = UserStatus.ACTIVE;
    }

    public void withdraw() {
        this.name = null;
        this.roomNumber = null;
        this.phoneNumber = null;
        this.nickname = WITHDRAWN_NICKNAME_PREFIX + id;
        this.status = UserStatus.WITHDRAWN;
    }
}
