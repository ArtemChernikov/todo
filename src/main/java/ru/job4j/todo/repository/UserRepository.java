package ru.job4j.todo.repository;

import ru.job4j.todo.model.entity.User;

import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.09.2023
 */
public interface UserRepository {

    Optional<User> create(User user);
    Optional<User> findByLoginAndPassword(String login, String password);
    Optional<User> findByLogin(String login);
    void deleteAll();

}
