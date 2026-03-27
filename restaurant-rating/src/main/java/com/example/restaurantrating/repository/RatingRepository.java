package com.example.restaurantrating.repository;

import com.example.restaurantrating.model.Rating;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RatingRepository {
    private final Map<Long, Rating> ratings = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void save(Rating rating) {
        Long id = idGenerator.getAndIncrement();
        rating.setId(id);
        ratings.put(id, rating);
    }

    public void save(Long id, Rating rating) {
        rating.setId(id);
        ratings.put(id, rating);
    }

    public boolean remove(Long id) {
        return ratings.remove(id) != null;
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratings.values());
    }

    public Rating findById(Long id) {
        return ratings.get(id);
    }

    public List<Rating> findByRestaurantId(Long restaurantId) {
        return ratings.values().stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();
    }

    public List<Rating> findByVisitorId(Long visitorId) {
        return ratings.values().stream()
                .filter(r -> r.getVisitorId().equals(visitorId))
                .toList();
    }
}