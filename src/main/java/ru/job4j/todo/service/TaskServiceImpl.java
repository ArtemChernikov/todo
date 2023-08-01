package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.repository.TaskRepository;
import ru.job4j.todo.util.mapper.TaskMapper;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.07.2023
 */
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    private final TaskMapper mapper;

    @Override
    public Optional<TaskDto> save(Task task) {
        Optional<Task> createdTask = repository.create(task);
        Optional<TaskDto> createdTaskDto = Optional.empty();

        if (createdTask.isPresent()) {
            TaskDto taskDto = mapper.taskToTaskDto(createdTask.get());
            createdTaskDto = Optional.of(taskDto);
        }
        return createdTaskDto;
    }

    @Override
    public boolean update(Task newTask) {
        return repository.update(newTask);
    }

    @Override
    public boolean deleteById(Integer taskId) {
        return repository.deleteById(taskId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean completeTask(Integer id) {
        return repository.completeTask(id);
    }

    @Override
    public Optional<TaskDto> getById(Integer taskId) {
        Optional<Task> findTask = repository.findById(taskId);
        Optional<TaskDto> findTaskDto = Optional.empty();

        if (findTask.isPresent()) {
            TaskDto taskDto = mapper.taskToTaskDto(findTask.get());
            findTaskDto = Optional.of(taskDto);
        }
        return findTaskDto;
    }

    @Override
    public List<TaskDto> getAll() {
        List<Task> taskList = repository.findAll();
        return mapper.taskListToTaskDtoList(taskList);
    }

    @Override
    public List<TaskDto> getAllCompletedTasks() {
        List<Task> taskList = repository.findAllCompletedTasks();
        return mapper.taskListToTaskDtoList(taskList);
    }

    @Override
    public List<TaskDto> getNewTasks() {
        List<Task> taskList = repository.findNewTasks();
        return mapper.taskListToTaskDtoList(taskList);
    }

}
