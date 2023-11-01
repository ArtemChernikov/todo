package ru.job4j.todo.service;

import ru.job4j.todo.model.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<TaskDto> save(TaskDto taskDto, List<Integer> categoriesIds);

    boolean update(TaskDto newTaskDto, List<Integer> categoriesIds);

    boolean deleteById(Integer taskId);

    void deleteAll();

    boolean completeTask(Integer id);

    Optional<TaskDto> getById(Integer taskId);

    List<TaskDto> getAll(String timezone);

    List<TaskDto> getAllCompletedTasks(String timezone);

    List<TaskDto> getNewTasks(String timezone);
}
