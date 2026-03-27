package com.example.restaurantrating.service;

import com.example.restaurantrating.dto.UserRequestDTO;
import com.example.restaurantrating.dto.UserResponseDTO;
import com.example.restaurantrating.model.Visitor;
import com.example.restaurantrating.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public UserResponseDTO createUser(@Valid UserRequestDTO request) {
        Visitor visitor = new Visitor();
        visitor.setName(request.name());
        visitor.setAge(request.age());
        visitor.setGender(request.gender());
        visitorRepository.save(visitor);
        return new UserResponseDTO(
                visitor.getId(),
                visitor.getName(),
                visitor.getAge(),
                visitor.getGender()
        );
    }

    public UserResponseDTO updateUser(Long id, @Valid UserRequestDTO request) {
        Visitor visitor = visitorRepository.findById(id);
        if (visitor == null) {
            throw new RuntimeException("Посетитель с ID " + id + " не найден");
        }
        visitor.setName(request.name());
        visitor.setAge(request.age());
        visitor.setGender(request.gender());
        return new UserResponseDTO(
                visitor.getId(),
                visitor.getName(),
                visitor.getAge(),
                visitor.getGender()
        );
    }

    public boolean deleteUser(Long id) {
        return visitorRepository.remove(id);
    }

    public List<UserResponseDTO> getAllUsers() {
        return visitorRepository.findAll().stream()
                .map(v -> new UserResponseDTO(
                        v.getId(),
                        v.getName(),
                        v.getAge(),
                        v.getGender()
                ))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        Visitor visitor = visitorRepository.findById(id);
        if (visitor == null) {
            throw new RuntimeException("Посетитель с ID " + id + " не найден");
        }
        return new UserResponseDTO(
                visitor.getId(),
                visitor.getName(),
                visitor.getAge(),
                visitor.getGender()
        );
    }

    @SuppressWarnings("unused")
    public Visitor findById(Long id) {
        return visitorRepository.findById(id);
    }
}