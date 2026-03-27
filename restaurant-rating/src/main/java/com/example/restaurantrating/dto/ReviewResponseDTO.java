package com.example.restaurantrating.dto;

public record ReviewResponseDTO(
        Long id,
        Long visitorId,
        Long restaurantId,
        Integer rating,
        String reviewText
) {}