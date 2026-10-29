package com.dormi.dormi_be.domain.meal.entity;

import com.dormi.dormi_be.domain.meal.enums.MealSource;
import com.dormi.dormi_be.domain.meal.enums.MealType;
import com.dormi.dormi_be.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(
        name = "meals",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_meals_meal_date_meal_type",
                columnNames = {"meal_date", "meal_type"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal extends BaseTimeEntity {

    public static final String MENU_DELIMITER = "|";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate mealDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MealType mealType;

    @Column(nullable = false, length = 500)
    private String menu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MealSource source;

    @Builder
    private Meal(LocalDate mealDate, MealType mealType, String menu, MealSource source) {
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.menu = menu;
        this.source = source;
    }

    public void update(String menu, MealSource source) {
        this.menu = menu;
        this.source = source;
    }
}
