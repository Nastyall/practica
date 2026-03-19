package com.example.restaurantrating.service;

import com.example.restaurantrating.model.Visitor;
import com.example.restaurantrating.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public void save(Visitor visitor) {
        if (visitor.getAge() == null || visitor.getGender() == null) {
            throw new IllegalArgumentException("Возраст и пол обязательны для заполнения");
        }
        visitorRepository.save(visitor);
    }

    public boolean remove(Long id) {
        return visitorRepository.remove(id);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }
}