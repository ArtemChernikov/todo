package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Category;
import ru.job4j.todo.model.entity.Priority;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.model.entity.User;
import ru.job4j.todo.repository.CategoryRepository;
import ru.job4j.todo.repository.PriorityRepository;
import ru.job4j.todo.repository.TaskRepository;
import ru.job4j.todo.repository.UserRepository;
import ru.job4j.todo.util.mapper.TaskMapper;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
    private final PriorityRepository priorityRepository;
    private final CategoryRepository categoryRepository;

    private final TaskMapper mapper;

    @Override
    public Optional<TaskDto> save(TaskDto taskDto, List<Integer> categoriesIds) {
        User user = getUserByLogin(taskDto.getUserLogin());
        Priority priority = getPriorityByName(taskDto.getPriorityName());
        Set<Category> categories = new HashSet<>(categoryRepository.findByIdIn(categoriesIds));
        Task task = mapper.taskDtoToTask(taskDto, user, priority);
        task.setCategories(categories);
        Optional<Task> createdTask = taskRepository.create(task);

        if (createdTask.isPresent()) {
            Set<String> categoryNames = getCategoryNames(categories);
            taskDto.setId(createdTask.get().getId());
            taskDto.setCategoryNames(categoryNames);
            return Optional.of(taskDto);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(TaskDto newTaskDto, List<Integer> categoriesIds) {
        User user = getUserByLogin(newTaskDto.getUserLogin());
        Priority priority = getPriorityByName(newTaskDto.getPriorityName());
        Set<Category> categories = new HashSet<>(categoryRepository.findByIdIn(categoriesIds));
        Task task = mapper.taskDtoToTask(newTaskDto, user, priority);
        task.setCategories(categories);
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
            Task task = findTask.get();
            Set<String> categoryNames = getCategoryNames(task.getCategories());
            TaskDto taskDto = mapper.taskToTaskDto(task, categoryNames);
            findTaskDto = Optional.of(taskDto);
        }
        return findTaskDto;
    }

    @Override
    public List<TaskDto> getAll(String timezone) {
        List<Task> taskList = taskRepository.findAll();
        return getTaskDtoListWithTimezone(taskList, timezone);
    }

    @Override
    public List<TaskDto> getAllCompletedTasks(String timezone) {
        List<Task> taskList = taskRepository.findTasksByDone(true);
        return getTaskDtoListWithTimezone(taskList, timezone);
    }

    @Override
    public List<TaskDto> getNewTasks(String timezone) {
        List<Task> taskList = taskRepository.findTasksByDone(false);
        return getTaskDtoListWithTimezone(taskList, timezone);
    }

    private User getUserByLogin(String userLogin) {
        Optional<User> optionalUser = userRepository.findByLogin(userLogin);
        return optionalUser.orElseThrow();
    }

    private Priority getPriorityByName(String name) {
        Optional<Priority> optionalPriority = priorityRepository.findByName(name);
        return optionalPriority.orElseThrow();
    }

    private Set<String> getCategoryNames(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toSet());
    }

    private List<TaskDto> getTaskDtoListWithTimezone(List<Task> tasks, String timezone) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : tasks) {
            TaskDto taskDto = mapper.taskToTaskDto(task, getCategoryNames(task.getCategories()));
            taskDto.setCreated(taskDto.getCreated().atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.of(timezone)).toLocalDateTime());
            taskDtoList.add(taskDto);
        }
        return taskDtoList;
    }

}
