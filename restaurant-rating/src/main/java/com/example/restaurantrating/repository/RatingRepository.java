package com.example.restaurantrating.repository;

import com.example.restaurantrating.model.Rating;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RatingRepository {
    private final List<Rating> ratings = new ArrayList<>();

    public void save(Rating rating) {
        ratings.add(rating);
    }

    public boolean remove(Rating rating) {
        return ratings.remove(rating);
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratings);
    }

    public Rating findById(Long id) {
        if (id != null && id >= 0 && id < ratings.size()) {
            return ratings.get(id.intValue());
        }
        return null;
    }

    public List<Rating> findByRestaurantId(Long restaurantId) {
        return ratings.stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();
    }
}