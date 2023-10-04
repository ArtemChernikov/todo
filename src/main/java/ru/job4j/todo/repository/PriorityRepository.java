package ru.job4j.todo.repository;

import ru.job4j.todo.model.entity.Priority;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.10.2023
 */
public interface PriorityRepository {

    Optional<Priority> create(Priority priority);

    Optional<Priority> findByName(String name);

    Optional<Priority> findById(Integer id);

    List<Priority> findAll();

    void deleteAll();
}
