package ru.job4j.todo.service;

import ru.job4j.todo.model.entity.Priority;

import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.10.2023
 */
public interface PriorityService {
    List<Priority> findAll();
}
