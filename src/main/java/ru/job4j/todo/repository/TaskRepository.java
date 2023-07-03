package ru.job4j.todo.repository;

import ru.job4j.todo.model.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> create(Task task);

    boolean update(Task newTask);

    boolean deleteById(Integer taskId);

    void deleteAll();

    Optional<Task> findById(Integer taskId);

    List<Task> findAll();

    List<Task> findAllCompletedTasks();

    List<Task> findNewTasks();
}
