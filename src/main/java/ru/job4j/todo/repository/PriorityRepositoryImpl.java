package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.entity.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.10.2023
 */
@Repository
@Slf4j
@AllArgsConstructor
public class PriorityRepositoryImpl implements PriorityRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Priority> create(Priority priority) {
        try {
            crudRepository.run(session -> session.persist(priority));
            return Optional.of(priority);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Priority> findByName(String name) {
        return crudRepository.optional("FROM Priority WHERE name = :name", Priority.class, Map.of("name", name));
    }

    @Override
    public Optional<Priority> findById(Integer id) {
        return crudRepository.optional("FROM Priority WHERE id = :id", Priority.class, Map.of("id", id));
    }

    @Override
    public List<Priority> findAll() {
        return crudRepository.list("FROM Priority", Priority.class);
    }

    @Override
    public void deleteAll() {
        crudRepository.run("DELETE FROM Priority");
    }
}
