package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.entity.User;
import ru.job4j.todo.repository.UserRepository;

import java.util.*;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.09.2023
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {
        return userRepository.create(user);
    }

    @Override
    public Optional<User> getByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public String getDefaultTimezone() {
        return TimeZone.getDefault().getID();
    }

    @Override
    public List<String> getTimezones() {
        return Arrays.asList(TimeZone.getAvailableIDs());
    }
}
