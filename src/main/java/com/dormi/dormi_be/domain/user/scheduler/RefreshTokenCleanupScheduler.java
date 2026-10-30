package com.dormi.dormi_be.domain.user.scheduler;

import com.dormi.dormi_be.domain.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    // 매일 새벽 4시에 만료된 리프레시 토큰 삭제
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    @Transactional
    public void deleteExpiredTokens() {
        int deleted = refreshTokenRepository.deleteAllExpiredBefore(LocalDateTime.now());
        log.info("만료된 리프레시 토큰 {}건 삭제", deleted);
    }
}
