package com.dormi.dormi_be.domain.meal.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NdhsMealCrawler {
    public List<CrawledDailyMeal> fetchWeek(LocalDate anyDateInWeek) throws IOException {
        Document doc = Jsoup.connect("https://www.ndhs.or.kr/site/main/schedule/calendar/week_menu_EP")
                .data("date", anyDateInWeek.toString())
                .userAgent("Mozilla/5.0")
                .timeout(10_000)
                .get();

        List<CrawledDailyMeal> result = new ArrayList<>();
        for (Element row : doc.select("li.flex-tb")) {
            Element dateEl = row.selectFirst(".date .flex-inner");
            if (dateEl == null) {
                log.warn("식단 날짜 요소를 찾지 못해 건너뜀 (사이트 구조 변경 가능성): {}", row.className());
                continue;
            }
            LocalDate date = LocalDate.parse(dateEl.text().trim());
            result.add(new CrawledDailyMeal(date,
                    menus(row, ".breakfast"),
                    menus(row, ".lunch"),
                    menus(row, ".dinner")));
        }
        result.forEach(meal -> log.info("크롤링 결과: {}", meal));
        return result;
    }

    private List<String> menus(Element row, String cls) {
        Element pre = row.selectFirst(cls + " pre");
        if (pre == null) return List.of();
        return pre.wholeText().lines().map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
}
