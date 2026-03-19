package com.example.restaurantrating.service;

import com.example.restaurantrating.model.Rating;
import com.example.restaurantrating.model.Restaurant;
import com.example.restaurantrating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantService restaurantService;

    public void save(Rating rating) {
        if (rating.getRating() < 1 || rating.getRating() > 5) {
            throw new IllegalArgumentException("Оценка должна быть от 1 до 5");
        }

        ratingRepository.save(rating);

        Restaurant restaurant = restaurantService.findById(rating.getRestaurantId());
        if (restaurant != null) {
            restaurant.updateAverageRating(BigDecimal.valueOf(rating.getRating()));
        }
    }

    public boolean remove(Rating rating) {
        Long restaurantId = rating.getRestaurantId();
        boolean removed = ratingRepository.remove(rating);
        if (removed) {
            recalculateRestaurantAverageRating(restaurantId);
        }
        return removed;
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(Long id) {
        return ratingRepository.findById(id);
    }

    private void recalculateRestaurantAverageRating(Long restaurantId) {
        List<Rating> restaurantRatings = ratingRepository.findByRestaurantId(restaurantId);
        Restaurant restaurant = restaurantService.findById(restaurantId);

        if (restaurant != null) {
            if (restaurantRatings.isEmpty()) {
                restaurant.setAverageRating(BigDecimal.ZERO);
                restaurant.setTotalRatings(0);
                restaurant.setSumRatings(BigDecimal.ZERO);
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (Rating r : restaurantRatings) {
                    sum = sum.add(BigDecimal.valueOf(r.getRating()));
                }
                restaurant.setSumRatings(sum);
                restaurant.setTotalRatings(restaurantRatings.size());
                restaurant.setAverageRating(sum.divide(BigDecimal.valueOf(restaurantRatings.size()),
                        2, java.math.RoundingMode.HALF_UP));
            }
        }
    }
}