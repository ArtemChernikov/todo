package ru.job4j.todo.util.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Priority;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TaskMapperTest {

    private final TaskMapper mapper;

    @Autowired
    TaskMapperTest(TaskMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void whenTaskToTaskDto() {
        User user = new User(1, "name", "login", "password");
        Priority priority = new Priority(1, "name", 1);
        Task task = new Task(1, user, "name", "desc", LocalDateTime.now(), true, priority);

        TaskDto taskDto = mapper.taskToTaskDto(task);

        assertThat(taskDto).usingRecursiveComparison().ignoringFields("userLogin", "user",
                "priority", "priorityName").isEqualTo(task);
    }

    @Test
    public void whenTaskDtoToTask() {
        User user = new User(1, "name", "login", "password");
        Priority priority = new Priority(1, "name", 1);
        TaskDto taskDto = new TaskDto(1, "login", "name", "desc", LocalDateTime.now(), true, "urgently");

        Task task = mapper.taskDtoToTask(taskDto, user, priority);

        assertThat(task).usingRecursiveComparison().ignoringFields("userLogin", "user",
                "priority", "priorityName").isEqualTo(taskDto);
    }

    @Test
    public void whenTaskListToTaskDtoList() {
        User user = new User(1, "name", "login", "password");
        Priority priority = new Priority(1, "name", 1);
        List<Task> taskList = List.of(new Task(1, user, "name", "desc", LocalDateTime.now(), true, priority),
                new Task(1, user, "name", "desc", LocalDateTime.now(), true, priority));

        List<TaskDto> taskDtoList = mapper.taskListToTaskDtoList(taskList);

        assertThat(taskList).usingRecursiveComparison().ignoringFields("userLogin", "user",
                "priority", "priorityName").isEqualTo(taskDtoList);
    }

}