package com.example.restaurantrating.config;

import com.example.restaurantrating.model.*;
import com.example.restaurantrating.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final RatingService ratingService;

    @PostConstruct
    public void init() {
        System.out.println("\nСИСТЕМА ОЦЕНКИ РЕСТОРАНОВ");

        testAddVisitors();
        testAddRestaurants();
        testAddRatings();
        testShowResultsWithRatings();

        testRemoveVisitor();
        testRemoveRestaurant();
        testRemoveRating();
        testFindRatingById();

        testShowResultsAfterRemoval();

        System.out.println("\nТЕСТИРОВАНИЕ ЗАВЕРШЕНО\n");
    }

    private void testAddVisitors() {
        System.out.println("\nПОСЕТИТЕЛИ");

        visitorService.save(new Visitor(null, "Иван Петров", 30, "Мужской"));
        visitorService.save(new Visitor(null, null, 25, "Женский"));
        visitorService.save(new Visitor(null, "Сергей Иванов", 35, "Мужской"));
        visitorService.save(new Visitor(null, "Анна Смирнова", 28, "Женский"));
        visitorService.save(new Visitor(null, "Дмитрий Козлов", 42, "Мужской"));
        visitorService.save(new Visitor(null, "Елена Попова", 33, "Женский"));
        visitorService.save(new Visitor(null, null, 29, "Мужской"));
        visitorService.save(new Visitor(null, "Ольга Новикова", 31, "Женский"));

        System.out.println("Добавлено посетителей: " + visitorService.findAll().size());
        visitorService.findAll().forEach(v ->
                System.out.println("  ID:" + v.getId() + " | Имя: " +
                        (v.getName() != null ? v.getName() : "Аноним") +
                        " | Возраст: " + v.getAge() + " | Пол: " + v.getGender()));
    }

    private void testAddRestaurants() {
        System.out.println("\nРЕСТОРАНЫ");

        restaurantService.save(new Restaurant(null, "Белые ночи",
                "Уютный ресторан итальянской кухни", CuisineType.ITALIAN,
                new BigDecimal("2500"), BigDecimal.ZERO));

        restaurantService.save(new Restaurant(null, "Старый город",
                "Аутентичная китайская кухня", CuisineType.CHINESE,
                new BigDecimal("1500"), BigDecimal.ZERO));

        restaurantService.save(new Restaurant(null, "У камина",
                "Японский ресторан с суши-баром", CuisineType.JAPANESE,
                new BigDecimal("2000"), BigDecimal.ZERO));

        restaurantService.save(new Restaurant(null, "Теремок",
                "Традиционная русская кухня", CuisineType.RUSSIAN,
                new BigDecimal("1200"), BigDecimal.ZERO));

        restaurantService.save(new Restaurant(null, "Rene",
                "Изысканная французская кухня", CuisineType.FRENCH,
                new BigDecimal("3500"), BigDecimal.ZERO));

        System.out.println("Добавлено ресторанов: " + restaurantService.findAll().size());
        restaurantService.findAll().forEach(r ->
                System.out.println("  ID:" + r.getId() + " | " + r.getName() +
                        " | Кухня: " + r.getCuisineType().getDisplayName() +
                        " | Средний чек: " + r.getAverageBill() + " руб."));
    }

    private void testAddRatings() {
        System.out.println("\nОЦЕНКИ");

        var visitors = visitorService.findAll();
        var restaurants = restaurantService.findAll();

        if (restaurants.size() < 5) {
            System.out.println("Ошибка: недостаточно ресторанов для добавления оценок");
            return;
        }

        System.out.println("Добавляем оценки для ресторанов:");

        System.out.println("  Ресторан '" + restaurants.getFirst().getName() + "':");
        ratingService.save(new Rating(visitors.getFirst().getId(), restaurants.getFirst().getId(), 5, "Отличная паста!"));
        System.out.println("    + Оценка 5 звезд от " + getVisitorName(visitors.getFirst()));
        ratingService.save(new Rating(visitors.get(1).getId(), restaurants.getFirst().getId(), 4, "Хорошо, но дорого"));
        System.out.println("    + Оценка 4 звезды от " + getVisitorName(visitors.get(1)));
        ratingService.save(new Rating(visitors.get(2).getId(), restaurants.getFirst().getId(), 5, "Супер место!"));
        System.out.println("    + Оценка 5 звезд от " + getVisitorName(visitors.get(2)));

        System.out.println("  Ресторан '" + restaurants.get(1).getName() + "':");
        ratingService.save(new Rating(visitors.get(3).getId(), restaurants.get(1).getId(), 5, "Лучшие утки по-пекински"));
        System.out.println("    + Оценка 5 звезд от " + getVisitorName(visitors.get(3)));
        ratingService.save(new Rating(visitors.get(4).getId(), restaurants.get(1).getId(), 4, "Очень вкусно"));
        System.out.println("    + Оценка 4 звезды от " + getVisitorName(visitors.get(4)));

        System.out.println("  Ресторан '" + restaurants.get(2).getName() + "':");
        ratingService.save(new Rating(visitors.get(5).getId(), restaurants.get(2).getId(), 5, "Отличные роллы"));
        System.out.println("    + Оценка 5 звезд от " + getVisitorName(visitors.get(5)));
        ratingService.save(new Rating(visitors.get(6).getId(), restaurants.get(2).getId(), 4, "Хорошо"));
        System.out.println("    + Оценка 4 звезды от " + getVisitorName(visitors.get(6)));

        System.out.println("  Ресторан '" + restaurants.get(3).getName() + "':");
        ratingService.save(new Rating(visitors.get(7).getId(), restaurants.get(3).getId(), 5, "Как у бабушки"));
        System.out.println("    + Оценка 5 звезд от " + getVisitorName(visitors.get(7)));
        ratingService.save(new Rating(visitors.getFirst().getId(), restaurants.get(3).getId(), 4, "Блины отличные"));
        System.out.println("    + Оценка 4 звезды от " + getVisitorName(visitors.getFirst()));
        ratingService.save(new Rating(visitors.get(1).getId(), restaurants.get(3).getId(), 5, "Борщ - огонь!"));
        System.out.println("    + Оценка 5 звезд от " + getVisitorName(visitors.get(1)));

        System.out.println("  Ресторан '" + restaurants.get(4).getName() + "':");
        ratingService.save(new Rating(visitors.get(2).getId(), restaurants.get(4).getId(), 5, "Изысканно"));
        System.out.println("    + Оценка 5 звезд от " + getVisitorName(visitors.get(2)));
        ratingService.save(new Rating(visitors.get(3).getId(), restaurants.get(4).getId(), 4, "Дорого, но вкусно"));
        System.out.println("    + Оценка 4 звезды от " + getVisitorName(visitors.get(3)));

        System.out.println("\nВсего добавлено оценок: " + ratingService.findAll().size());
    }

    private String getVisitorName(Visitor visitor) {
        return visitor.getName() != null ? visitor.getName() : "Аноним (ID:" + visitor.getId() + ")";
    }

    private void testShowResultsWithRatings() {
        System.out.println("\nРЕЗУЛЬТАТ С ОЦЕНКАМИ");

        restaurantService.findAll().forEach(r -> {
            System.out.println("\n  Ресторан: " + r.getName());
            System.out.println("     Кухня: " + r.getCuisineType().getDisplayName());
            System.out.println("     Средний чек: " + r.getAverageBill() + " руб.");
            System.out.println("     СРЕДНЯЯ ОЦЕНКА: " + r.getAverageRating() + " из 5");
        });
    }

    private void testRemoveVisitor() {
        System.out.println("\nТЕСТИРОВАНИЕ VisitorService.remove()");

        var visitors = visitorService.findAll();
        if (visitors.size() > 1) {
            Visitor visitorToRemove = visitors.get(1);
            System.out.println("Удаляем посетителя с ID: " + visitorToRemove.getId() +
                    " (Имя: " + (visitorToRemove.getName() != null ? visitorToRemove.getName() : "Аноним") + ")");

            boolean removed = visitorService.remove(visitorToRemove.getId());
            System.out.println("Результат удаления: " + (removed ? "Успешно" : "Ошибка"));
            System.out.println("Осталось посетителей: " + visitorService.findAll().size());
        }
    }

    private void testRemoveRestaurant() {
        System.out.println("\nТЕСТИРОВАНИЕ RestaurantService.remove()");

        var restaurants = restaurantService.findAll();
        if (restaurants.size() > 3) {
            Restaurant restaurantToRemove = restaurants.get(3);
            System.out.println("Удаляем ресторан с ID: " + restaurantToRemove.getId() +
                    " (Название: " + restaurantToRemove.getName() + ")");

            boolean removed = restaurantService.remove(restaurantToRemove.getId());
            System.out.println("Результат удаления: " + (removed ? "Успешно" : "Ошибка"));
            System.out.println("Осталось ресторанов: " + restaurantService.findAll().size());
        }
    }

    private void testRemoveRating() {
        System.out.println("\nТЕСТИРОВАНИЕ RatingService.remove()");

        var ratings = ratingService.findAll();
        if (!ratings.isEmpty()) {
            Rating ratingToRemove = ratings.getFirst();
            System.out.println("Удаляем оценку: посетитель ID:" + ratingToRemove.getVisitorId() +
                    ", ресторан ID:" + ratingToRemove.getRestaurantId() +
                    ", оценка: " + ratingToRemove.getRating() + " звезд");

            boolean removed = ratingService.remove(ratingToRemove);
            System.out.println("Результат удаления: " + (removed ? "Успешно" : "Ошибка"));
            System.out.println("Осталось оценок: " + ratingService.findAll().size());
        }
    }

    private void testFindRatingById() {
        System.out.println("\nТЕСТИРОВАНИЕ RatingService.findById()");

        var ratings = ratingService.findAll();
        if (!ratings.isEmpty()) {
            Long index = 0L;
            System.out.println("Ищем оценку по индексу: " + index);

            Rating foundRating = ratingService.findById(index);

            if (foundRating != null) {
                System.out.println("Оценка найдена:");
                System.out.println("  Посетитель ID: " + foundRating.getVisitorId());
                System.out.println("  Ресторан ID: " + foundRating.getRestaurantId());
                System.out.println("  Оценка: " + foundRating.getRating() + " звезд");
                System.out.println("  Отзыв: " + foundRating.getReviewText());
            } else {
                System.out.println("Оценка не найдена");
            }
        }
    }

    private void testShowResultsAfterRemoval() {
        System.out.println("\nРезультаты ПОСЛЕ удалений");

        restaurantService.findAll().forEach(r -> {
            System.out.println("\n  Ресторан: " + r.getName());
            System.out.println("     Кухня: " + r.getCuisineType().getDisplayName());
            System.out.println("     Средний чек: " + r.getAverageBill() + " руб.");
            System.out.println("     ОБНОВЛЕННАЯ СРЕДНЯЯ ОЦЕНКА: " + r.getAverageRating() + " из 5");
        });
    }
}