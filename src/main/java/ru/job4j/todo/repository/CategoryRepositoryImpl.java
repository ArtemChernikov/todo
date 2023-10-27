package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.entity.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 25.10.2023
 */
@Repository
@Slf4j
@AllArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Category> findById(Integer id) {
        return crudRepository.optional("FROM Category WHERE id = :id", Category.class, Map.of("id", id));
    }

    @Override
    public List<Category> findAll() {
        return crudRepository.list("FROM Category", Category.class);
    }

    @Override
    public List<Category> findByIdIn(List<Integer> idList) {
        return crudRepository.list("FROM Category c WHERE c.id IN :idList", idList, Category.class);
    }
}
