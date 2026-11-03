package com.dormi.dormi_be.domain.meal.scheduler;

import com.dormi.dormi_be.domain.meal.service.MealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class MealCrawlScheduler {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final MealService mealService;

    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")   // 매일 05:00
    public void crawlMeals() {
        LocalDate today = LocalDate.now(SEOUL);
        mealService.crawlAndSave(today);                // 이번 주
        mealService.crawlAndSave(today.plusWeeks(1));   // 다음 주
    }

    @EventListener(ApplicationReadyEvent.class)
    public void crawlMealsOnStartup() {
        try {
            crawlMeals();
        } catch (Exception e) {
            // 수집 실패가 서버 기동을 막지 않도록 한다
            log.error("서버 시작 시 식단 수집 실패", e);
        }
    }
}
