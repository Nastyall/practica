package com.example.restaurantrating.repository;

import com.example.restaurantrating.model.Visitor;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VisitorRepository {
    private final Map<Long, Visitor> visitors = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(idGenerator.getAndIncrement());
        }
        visitors.put(visitor.getId(), visitor);
    }

    public boolean remove(Long id) {
        return visitors.remove(id) != null;
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors.values());
    }

    public Visitor findById(Long id) {
        return visitors.get(id);
    }
}