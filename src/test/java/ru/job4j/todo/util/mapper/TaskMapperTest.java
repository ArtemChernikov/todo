package ru.job4j.todo.util.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Category;
import ru.job4j.todo.model.entity.Priority;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TaskMapperTest {

    private static final String TIMEZONE = TimeZone.getDefault().getID();

    private final TaskMapper mapper;

    @Autowired
    TaskMapperTest(TaskMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    void whenTaskToTaskDto() {
        User user = new User(1, "name", "login", "password", TIMEZONE);
        Priority priority = new Priority(1, "name", 1);
        Category category = new Category(1, "Работа");
        Task task = new Task(1, user, "name", "desc", LocalDateTime.now(), true, priority,
                Set.of(category));

        TaskDto taskDto = mapper.taskToTaskDto(task, Set.of(category.getName()));

        assertThat(taskDto).usingRecursiveComparison().ignoringFields("userLogin", "user",
                "priority", "priorityName", "categories", "categoryNames").isEqualTo(task);
    }

    @Test
    void whenTaskDtoToTask() {
        User user = new User(1, "name", "login", "password", TIMEZONE);
        Priority priority = new Priority(1, "name", 1);
        Category category = new Category(1, "Работа");
        TaskDto taskDto = new TaskDto(1, "login", "name", "desc", LocalDateTime.now(),
                true, "urgently", Set.of(category.getName()));

        Task task = mapper.taskDtoToTask(taskDto, user, priority);

        assertThat(task).usingRecursiveComparison().ignoringFields("userLogin", "user",
                "priority", "priorityName", "categories", "categoryNames").isEqualTo(taskDto);
    }

    @Test
    void whenTaskListToTaskDtoList() {
        User user = new User(1, "name", "login", "password", TIMEZONE);
        Priority priority = new Priority(1, "name", 1);
        Category category = new Category(1, "Работа");
        List<Task> taskSet = List.of(new Task(1, user, "name", "desc", LocalDateTime.now(),
                        true, priority, Set.of(category)),
                new Task(1, user, "name", "desc", LocalDateTime.now(),
                        true, priority, Set.of(category)));

        List<TaskDto> taskDtoList = mapper.taskListToTaskDtoList(taskSet);

        assertThat(taskSet).usingRecursiveComparison().ignoringFields("userLogin", "user",
                "priority", "priorityName", "categories", "categoryNames").isEqualTo(taskDtoList);
    }

}