package com.dormi.dormi_be.domain.meal.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DailyMealResDto (
        LocalDate date,
        List<String> breakfast,
        List<String> lunch,
        List<String> dinner
){
}
