package com.dormi.dormi_be.domain.meal.controller;

import com.dormi.dormi_be.domain.meal.dto.response.DailyMealResDto;
import com.dormi.dormi_be.domain.meal.service.MealService;
import com.dormi.dormi_be.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Meal", description = "급식 API")
@RestController
@RequestMapping("/api/meal")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @GetMapping
    public BaseResponse<DailyMealResDto> getDailyMeal(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return BaseResponse.onSuccess(mealService.getDailyMeal(date));
    }

    @GetMapping("/week")
    public BaseResponse<List<DailyMealResDto>> getWeeklyMeal(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return BaseResponse.onSuccess(mealService.getWeeklyMeal(date));
    }
}
