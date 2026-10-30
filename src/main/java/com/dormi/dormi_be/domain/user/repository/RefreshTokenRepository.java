package com.dormi.dormi_be.domain.user.repository;

import com.dormi.dormi_be.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Long userId);

    @Modifying
    @Query("delete from RefreshToken r where r.expiresAt < :now")
    int deleteAllExpiredBefore(@Param("now") LocalDateTime now);
}
