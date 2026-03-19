package com.example.restaurantrating.repository;

import com.example.restaurantrating.model.Visitor;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(idGenerator.getAndIncrement());
        }
        visitors.add(visitor);
    }

    public boolean remove(Long id) {
        return visitors.removeIf(visitor -> visitor.getId().equals(id));
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }
}