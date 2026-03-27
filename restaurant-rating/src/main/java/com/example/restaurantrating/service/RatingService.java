package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.ReviewRequestDTO;
import com.example.restaurantrating.dto.ReviewResponseDTO;
import com.example.restaurantrating.model.Rating;
import com.example.restaurantrating.model.Restaurant;
import com.example.restaurantrating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantService restaurantService;
    private final VisitorService visitorService;

    public ReviewResponseDTO createReview(@Valid ReviewRequestDTO request) {
        visitorService.getUserById(request.visitorId());
        restaurantService.getRestaurantById(request.restaurantId());

        Rating rating = new Rating();
        rating.setVisitorId(request.visitorId());
        rating.setRestaurantId(request.restaurantId());
        rating.setRating(request.rating());
        rating.setReviewText(request.reviewText());

        ratingRepository.save(rating);
        restaurantService.updateAverageRating(request.restaurantId(), BigDecimal.valueOf(request.rating()));

        return new ReviewResponseDTO(
                rating.getId(),
                rating.getVisitorId(),
                rating.getRestaurantId(),
                rating.getRating(),
                rating.getReviewText()
        );
    }

    public ReviewResponseDTO updateReview(Long id, @Valid ReviewRequestDTO request) {
        Rating existingRating = ratingRepository.findById(id);
        if (existingRating == null) {
            throw new RuntimeException("Отзыв с ID " + id + " не найден");
        }

        visitorService.getUserById(request.visitorId());
        restaurantService.getRestaurantById(request.restaurantId());

        existingRating.setVisitorId(request.visitorId());
        existingRating.setRestaurantId(request.restaurantId());
        existingRating.setRating(request.rating());
        existingRating.setReviewText(request.reviewText());

        ratingRepository.save(id, existingRating);
        recalculateRestaurantAverageRating(request.restaurantId());

        return new ReviewResponseDTO(
                id,
                existingRating.getVisitorId(),
                existingRating.getRestaurantId(),
                existingRating.getRating(),
                existingRating.getReviewText()
        );
    }

    public boolean deleteReview(Long id) {
        Rating rating = ratingRepository.findById(id);
        if (rating == null) {
            return false;
        }
        boolean removed = ratingRepository.remove(id);
        if (removed) {
            recalculateRestaurantAverageRating(rating.getRestaurantId());
        }
        return removed;
    }

    public List<ReviewResponseDTO> getAllReviews() {
        return ratingRepository.findAll().stream()
                .map(r -> new ReviewResponseDTO(
                        r.getId(),
                        r.getVisitorId(),
                        r.getRestaurantId(),
                        r.getRating(),
                        r.getReviewText()
                ))
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO getReviewById(Long id) {
        Rating rating = ratingRepository.findById(id);
        if (rating == null) {
            throw new RuntimeException("Отзыв с ID " + id + " не найден");
        }
        return new ReviewResponseDTO(
                rating.getId(),
                rating.getVisitorId(),
                rating.getRestaurantId(),
                rating.getRating(),
                rating.getReviewText()
        );
    }

    public List<ReviewResponseDTO> getReviewsByRestaurant(Long restaurantId) {
        return ratingRepository.findByRestaurantId(restaurantId).stream()
                .map(r -> new ReviewResponseDTO(
                        r.getId(),
                        r.getVisitorId(),
                        r.getRestaurantId(),
                        r.getRating(),
                        r.getReviewText()
                ))
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDTO> getReviewsByUser(Long userId) {
        return ratingRepository.findByVisitorId(userId).stream()
                .map(r -> new ReviewResponseDTO(
                        r.getId(),
                        r.getVisitorId(),
                        r.getRestaurantId(),
                        r.getRating(),
                        r.getReviewText()
                ))
                .collect(Collectors.toList());
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