package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.entity.User;

import java.util.Map;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.09.2023
 */
@Slf4j
@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> create(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("FROM User WHERE login = :login and password = :password", User.class,
                Map.of("login", login, "password", password));
    }
}
