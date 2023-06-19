package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task create(Task task);

    boolean update(Task newTask);

    boolean deleteById(Integer taskId);

    Optional<Task> findById(Integer taskId);

    List<Task> findAll();
}
