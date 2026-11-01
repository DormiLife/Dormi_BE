package com.dormi.dormi_be.domain.meal.repository;

import com.dormi.dormi_be.domain.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findAllByMealDateBetween(LocalDate from, LocalDate to);
}
