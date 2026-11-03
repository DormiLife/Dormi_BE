package com.dormi.dormi_be.domain.meal.crawler;

import java.time.LocalDate;
import java.util.List;

public record CrawledDailyMeal(
        LocalDate date,
        List<String> breakfast,
        List<String> lunch,
        List<String> dinner
) {

}
