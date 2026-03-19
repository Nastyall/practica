package com.example.restaurantrating.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CuisineType {
    ITALIAN("Итальянская"),
    CHINESE("Китайская"),
    JAPANESE("Японская"),
    RUSSIAN("Русская"),
    FRENCH("Французская");

    private final String displayName;
}