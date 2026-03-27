package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.RestaurantRequestDTO;
import com.example.restaurantrating.dto.RestaurantResponseDTO;
import com.example.restaurantrating.model.CuisineType;
import com.example.restaurantrating.model.Restaurant;
import com.example.restaurantrating.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantResponseDTO createRestaurant(@Valid RestaurantRequestDTO request) {
        CuisineType cuisineType;
        try {
            cuisineType = CuisineType.valueOf(request.cuisineType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Некорректный тип кухни. Доступные: ITALIAN, CHINESE, JAPANESE, RUSSIAN, FRENCH");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setCuisineType(cuisineType);
        restaurant.setAverageBill(request.averageBill());
        restaurant.setAverageRating(BigDecimal.ZERO);
        restaurant.setTotalRatings(0);
        restaurant.setSumRatings(BigDecimal.ZERO);

        restaurantRepository.save(restaurant);

        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType().getDisplayName(),
                restaurant.getAverageBill(),
                restaurant.getAverageRating(),
                restaurant.getTotalRatings()
        );
    }

    public RestaurantResponseDTO updateRestaurant(Long id, @Valid RestaurantRequestDTO request) {
        Restaurant restaurant = findRestaurantById(id);
        if (restaurant == null) {
            throw new RuntimeException("Ресторан с ID " + id + " не найден");
        }

        CuisineType cuisineType;
        try {
            cuisineType = CuisineType.valueOf(request.cuisineType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Некорректный тип кухни");
        }

        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setCuisineType(cuisineType);
        restaurant.setAverageBill(request.averageBill());

        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType().getDisplayName(),
                restaurant.getAverageBill(),
                restaurant.getAverageRating(),
                restaurant.getTotalRatings()
        );
    }

    public boolean deleteRestaurant(Long id) {
        return restaurantRepository.remove(id);
    }

    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(r -> new RestaurantResponseDTO(
                        r.getId(),
                        r.getName(),
                        r.getDescription(),
                        r.getCuisineType().getDisplayName(),
                        r.getAverageBill(),
                        r.getAverageRating(),
                        r.getTotalRatings()
                ))
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO getRestaurantById(Long id) {
        Restaurant restaurant = findRestaurantById(id);
        if (restaurant == null) {
            throw new RuntimeException("Ресторан с ID " + id + " не найден");
        }
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType().getDisplayName(),
                restaurant.getAverageBill(),
                restaurant.getAverageRating(),
                restaurant.getTotalRatings()
        );
    }

    @SuppressWarnings("unused")
    public Restaurant findById(Long id) {
        return findRestaurantById(id);
    }

    private Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void updateAverageRating(Long restaurantId, BigDecimal newRating) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (restaurant != null) {
            restaurant.updateAverageRating(newRating);
        }
    }
}