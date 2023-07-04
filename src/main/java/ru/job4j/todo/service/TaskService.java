package ru.job4j.todo.service;

import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<TaskDto> save(Task task);

    boolean update(Task newTask);

    boolean deleteById(Integer taskId);

    void deleteAll();

    Optional<TaskDto> getById(Integer taskId);

    List<TaskDto> getAll();

    List<TaskDto> getAllCompletedTasks();

    List<TaskDto> getNewTasks();
}
