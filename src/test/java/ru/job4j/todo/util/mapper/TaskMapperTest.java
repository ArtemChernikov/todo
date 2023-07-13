package ru.job4j.todo.util.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Task;

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
        Task task = new Task(1, "name", "desc", LocalDateTime.now(), true);

        TaskDto taskDto = mapper.taskToTaskDto(task);

        assertThat(taskDto).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenTaskDtoToTask() {
        TaskDto taskDto = new TaskDto(1, "name", "desc", LocalDateTime.now(), true);

        Task task = mapper.taskDtoToTask(taskDto);

        assertThat(task).usingRecursiveComparison().isEqualTo(taskDto);
    }

    @Test
    public void whenTaskListToTaskDtoList() {
        List<Task> taskList = List.of(new Task(1, "name", "desc", LocalDateTime.now(), true),
                new Task(1, "name", "desc", LocalDateTime.now(), true));

        List<TaskDto> taskDtoList = mapper.taskListToTaskDtoList(taskList);

        assertThat(taskList).usingRecursiveComparison().isEqualTo(taskDtoList);
    }

    @Test
    public void whenTaskDtoListToTaskList() {
        List<TaskDto> taskDtoList = List.of(new TaskDto(1, "name", "desc", LocalDateTime.now(), true),
                new TaskDto(1, "name", "desc", LocalDateTime.now(), true));

        List<Task> taskList = mapper.taskDtoListToTaskList(taskDtoList);

        assertThat(taskDtoList).usingRecursiveComparison().isEqualTo(taskList);
    }

}