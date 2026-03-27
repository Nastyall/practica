package com.example.restaurantrating.controller;

import com.example.restaurantrating.dto.ReviewRequestDTO;
import com.example.restaurantrating.dto.ReviewResponseDTO;
import com.example.restaurantrating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Отзывы", description = "Управление отзывами и оценками")
@SuppressWarnings({"unused", "NullableProblems"})
public class ReviewController {
    private final RatingService ratingService;

    @PostMapping
    @Operation(summary = "Создать новый отзыв")
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO review = ratingService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(ratingService.getAllReviews());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getReviewById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Получить все отзывы ресторана")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(ratingService.getReviewsByRestaurant(restaurantId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить все отзывы посетителя")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getReviewsByUser(userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO request) {
        return ResponseEntity.ok(ratingService.updateReview(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        boolean deleted = ratingService.deleteReview(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}