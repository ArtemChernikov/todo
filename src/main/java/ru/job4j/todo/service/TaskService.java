package ru.job4j.todo.service;

import ru.job4j.todo.model.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<TaskDto> save(TaskDto taskDto);

    boolean update(TaskDto newTaskDto);

    boolean deleteById(Integer taskId);

    void deleteAll();

    boolean completeTask(Integer id);

    Optional<TaskDto> getById(Integer taskId);

    List<TaskDto> getAll();

    List<TaskDto> getAllCompletedTasks();

    List<TaskDto> getNewTasks();
}
