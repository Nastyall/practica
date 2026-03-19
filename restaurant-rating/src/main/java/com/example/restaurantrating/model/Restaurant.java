package com.example.restaurantrating.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private Long id;
    private String name;
    private String description;
    private CuisineType cuisineType;
    private BigDecimal averageBill;
    private BigDecimal averageRating;
    private int totalRatings;
    private BigDecimal sumRatings;

    public Restaurant(Long id, String name, String description, CuisineType cuisineType,
                      BigDecimal averageBill, BigDecimal averageRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cuisineType = cuisineType;
        this.averageBill = averageBill;
        this.averageRating = averageRating;
        this.totalRatings = 0;
        this.sumRatings = BigDecimal.ZERO;
    }

    public void updateAverageRating(BigDecimal newRating) {
        if (this.sumRatings == null) {
            this.sumRatings = BigDecimal.ZERO;
        }
        if (this.totalRatings == 0) {
            this.sumRatings = newRating;
            this.totalRatings = 1;
            this.averageRating = newRating.setScale(2, RoundingMode.HALF_UP);
        } else {
            this.sumRatings = this.sumRatings.add(newRating);
            this.totalRatings++;
            this.averageRating = this.sumRatings
                    .divide(BigDecimal.valueOf(totalRatings), 2, RoundingMode.HALF_UP);
        }
    }
}