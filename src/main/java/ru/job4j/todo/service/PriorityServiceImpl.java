package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.entity.Priority;
import ru.job4j.todo.repository.PriorityRepository;

import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.10.2023
 */
@Service
@AllArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;

    @Override
    public List<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
