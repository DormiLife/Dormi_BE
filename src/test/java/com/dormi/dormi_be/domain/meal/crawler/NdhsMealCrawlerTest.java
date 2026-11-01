package com.dormi.dormi_be.domain.meal.crawler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class NdhsMealCrawlerTest {

    @Test
    void fetchWeek() throws IOException {
        List<CrawledDailyMeal> meals = new NdhsMealCrawler().fetchWeek(LocalDate.now());

        meals.forEach(meal -> log.info("{}", meal));
        assertThat(meals).hasSize(7);
    }
}
