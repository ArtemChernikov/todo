package ru.job4j.todo.repository;

import ru.job4j.todo.model.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 25.10.2023
 */
public interface CategoryRepository {
    Optional<Category> findById(Integer id);

    List<Category> findAll();

    List<Category> findByIdIn(List<Integer> categoriesIds);
}
