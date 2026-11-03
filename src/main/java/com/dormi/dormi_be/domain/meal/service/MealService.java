package com.dormi.dormi_be.domain.meal.service;

import com.dormi.dormi_be.domain.meal.crawler.CrawledDailyMeal;
import com.dormi.dormi_be.domain.meal.crawler.NdhsMealCrawler;
import com.dormi.dormi_be.domain.meal.dto.response.DailyMealResDto;
import com.dormi.dormi_be.domain.meal.entity.Meal;
import com.dormi.dormi_be.domain.meal.enums.MealSource;
import com.dormi.dormi_be.domain.meal.enums.MealType;
import com.dormi.dormi_be.domain.meal.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MealService {

    private final MealRepository mealRepository;
    private final NdhsMealCrawler ndhsMealCrawler;

    /**
     * anyDateInWeek가 속한 주의 식단을 크롤링해서 저장한다. 이미 있는 (날짜, 끼니)는 갱신한다.
     * 네트워크 오류 등으로 크롤링에 실패하면 로그만 남기고 반환한다.
     */
    @Transactional
    public void crawlAndSave(LocalDate anyDateInWeek) {
        List<CrawledDailyMeal> crawled;
        try {
            crawled = ndhsMealCrawler.fetchWeek(anyDateInWeek);
        } catch (IOException e) {
            log.error("식단 크롤링 실패: date={}", anyDateInWeek, e);
            return;
        }
        if (crawled.isEmpty()) {
            log.warn("크롤링된 식단이 없음: date={}", anyDateInWeek);
            return;
        }

        LocalDate from = crawled.stream().map(CrawledDailyMeal::date).min(Comparator.naturalOrder()).get();
        LocalDate to = crawled.stream().map(CrawledDailyMeal::date).max(Comparator.naturalOrder()).get();

        Map<MealKey, Meal> existing = new HashMap<>();
        for (Meal meal : mealRepository.findAllByMealDateBetween(from, to)) {
            existing.put(new MealKey(meal.getMealDate(), meal.getMealType()), meal);
        }

        List<Meal> toInsert = new ArrayList<>();
        for (CrawledDailyMeal day : crawled) {
            upsert(day.date(), MealType.BREAKFAST, day.breakfast(), existing, toInsert);
            upsert(day.date(), MealType.LUNCH, day.lunch(), existing, toInsert);
            upsert(day.date(), MealType.DINNER, day.dinner(), existing, toInsert);
        }
        mealRepository.saveAll(toInsert);
        log.info("식단 저장 완료: {} ~ {}, 신규 {}건", from, to, toInsert.size());
    }

    private void upsert(LocalDate date, MealType type, List<String> menus,
                        Map<MealKey, Meal> existing, List<Meal> toInsert) {
        if (menus.isEmpty()) {
            return;
        }
        String menu = String.join(Meal.MENU_DELIMITER, menus);

        Meal found = existing.get(new MealKey(date, type));
        if (found == null) {
            toInsert.add(Meal.builder()
                    .mealDate(date)
                    .mealType(type)
                    .menu(menu)
                    .source(MealSource.CRAWLED)
                    .build());
            return;
        }
        // 수동으로 입력한 식단은 크롤링 결과로 덮어쓰지 않는다
        if (found.getSource() == MealSource.MANUAL) {
            return;
        }
        found.update(menu, MealSource.CRAWLED);
    }

    private record MealKey(LocalDate date, MealType type) {
    }

    public DailyMealResDto getDailyMeal(LocalDate date) {
        return findDailyMeals(date, date).get(0);
    }

    /** 주의 시작은 월요일이다. 해당 주의 아무 날짜나 넘기면 월~일 7일치를 돌려준다. */
    public List<DailyMealResDto> getWeeklyMeal(LocalDate anyDateInWeek) {
        LocalDate monday = anyDateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return findDailyMeals(monday, monday.plusDays(6));
    }

    private List<DailyMealResDto> findDailyMeals(LocalDate from, LocalDate to) {
        Map<LocalDate, Map<MealType, String>> menusByDate = new HashMap<>();
        for (Meal meal : mealRepository.findAllByMealDateBetween(from, to)) {
            menusByDate.computeIfAbsent(meal.getMealDate(), d -> new EnumMap<>(MealType.class))
                    .put(meal.getMealType(), meal.getMenu());
        }

        // 데이터가 없는 날도 빈 메뉴로 채워서 항상 from~to의 모든 날짜를 돌려준다
        return from.datesUntil(to.plusDays(1))
                .map(date -> {
                    Map<MealType, String> menus = menusByDate.getOrDefault(date, Map.of());
                    return DailyMealResDto.builder()
                            .date(date)
                            .breakfast(splitMenu(menus.get(MealType.BREAKFAST)))
                            .lunch(splitMenu(menus.get(MealType.LUNCH)))
                            .dinner(splitMenu(menus.get(MealType.DINNER)))
                            .build();
                })
                .toList();
    }

    private List<String> splitMenu(String menu) {
        if (menu == null) {
            return List.of();
        }
        return List.of(menu.split(Pattern.quote(Meal.MENU_DELIMITER)));
    }
}
