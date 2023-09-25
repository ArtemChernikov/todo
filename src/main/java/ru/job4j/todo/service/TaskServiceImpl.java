package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.model.entity.User;
import ru.job4j.todo.repository.TaskRepository;
import ru.job4j.todo.repository.UserRepository;
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

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final TaskMapper mapper;

    @Override
    public Optional<TaskDto> save(TaskDto taskDto) {
        User user = getUserByLogin(taskDto.getUserLogin());
        Task task = mapper.taskDtoToTask(taskDto, user);
        Optional<Task> createdTask = taskRepository.create(task);

        if (createdTask.isPresent()) {
            taskDto.setId(createdTask.get().getId());
            return Optional.of(taskDto);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(TaskDto newTaskDto) {
        System.out.println(newTaskDto.getUserLogin());
        User user = getUserByLogin(newTaskDto.getUserLogin());
        Task task = mapper.taskDtoToTask(newTaskDto, user);
        return taskRepository.update(task);
    }

    @Override
    public boolean deleteById(Integer taskId) {
        return taskRepository.deleteById(taskId);
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }

    @Override
    public boolean completeTask(Integer id) {
        return taskRepository.completeTask(id);
    }

    @Override
    public Optional<TaskDto> getById(Integer taskId) {
        Optional<Task> findTask = taskRepository.findById(taskId);
        Optional<TaskDto> findTaskDto = Optional.empty();

        if (findTask.isPresent()) {
            TaskDto taskDto = mapper.taskToTaskDto(findTask.get());
            findTaskDto = Optional.of(taskDto);
        }
        return findTaskDto;
    }

    @Override
    public List<TaskDto> getAll() {
        List<Task> taskList = taskRepository.findAll();
        return mapper.taskListToTaskDtoList(taskList);
    }

    @Override
    public List<TaskDto> getAllCompletedTasks() {
        List<Task> taskList = taskRepository.findTasksByDone(true);
        return mapper.taskListToTaskDtoList(taskList);
    }

    @Override
    public List<TaskDto> getNewTasks() {
        List<Task> taskList = taskRepository.findTasksByDone(false);
        return mapper.taskListToTaskDtoList(taskList);
    }

    private User getUserByLogin(String userLogin) {
        Optional<User> optionalUser = userRepository.findByLogin(userLogin);
        return optionalUser.orElseThrow();
    }

}
