package com.example.restaurantrating.repository;

import com.example.restaurantrating.model.Restaurant;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(idGenerator.getAndIncrement());
        }
        restaurants.add(restaurant);
    }

    public boolean remove(Long id) {
        return restaurants.removeIf(restaurant -> restaurant.getId().equals(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }
}