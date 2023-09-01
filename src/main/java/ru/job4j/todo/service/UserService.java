package ru.job4j.todo.service;

import ru.job4j.todo.model.entity.User;

import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.09.2023
 */
public interface UserService {

    Optional<User> save(User user);

    Optional<User> getByLoginAndPassword(String login, String password);

}
