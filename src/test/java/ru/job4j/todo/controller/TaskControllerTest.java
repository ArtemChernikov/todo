package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Category;
import ru.job4j.todo.model.entity.Priority;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;
    private PriorityService priorityService;
    private CategoryService categoryService;

    @BeforeEach
    public void init() {
        taskService = mock(TaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        taskController = new TaskController(taskService, priorityService, categoryService);
    }

    @Test
    void whenGetTasks() {
        Category category = new Category(1, "Работа");
        TaskDto task1 = new TaskDto(1, "login", "name1", "desc1", LocalDateTime.now(),
                true, "urgently", Set.of(category.getName()));
        TaskDto task2 = new TaskDto(1, "login", "name2", "desc2", LocalDateTime.now(),
                true, "urgently", Set.of(category.getName()));
        List<TaskDto> taskDtoList = List.of(task1, task2);
        when(taskService.getAll()).thenReturn(taskDtoList);

        Model model = new ConcurrentModel();
        String view = taskController.getTasks(model);
        Object tasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(tasks).usingRecursiveComparison().isEqualTo(taskDtoList);
    }

    @Test
    void whenGetCreationPage() {
        List<Priority> expectedPriorities = List.of(new Priority(1, "urgently", 1));
        when(priorityService.findAll()).thenReturn(expectedPriorities);

        Model model = new ConcurrentModel();
        String view = taskController.getCreationPage(model);
        Object actualPriorities = model.getAttribute("priorities");

        assertThat(view).isEqualTo("tasks/create");
        assertThat(actualPriorities).usingRecursiveComparison().isEqualTo(expectedPriorities);
    }

    @Test
    void whenGetCompletedTasks() {
        Category category = new Category(1, "Работа");
        TaskDto task1 = new TaskDto(1, "login", "name1", "desc1", LocalDateTime.now(),
                true, "urgently", Set.of(category.getName()));
        TaskDto task2 = new TaskDto(1, "login", "name2", "desc2", LocalDateTime.now(),
                true, "urgently", Set.of(category.getName()));
        List<TaskDto> taskDtoList = List.of(task1, task2);
        when(taskService.getAllCompletedTasks()).thenReturn(taskDtoList);

        Model model = new ConcurrentModel();
        String view = taskController.getCompletedTasks(model);
        Object tasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(tasks).usingRecursiveComparison().isEqualTo(taskDtoList);
    }

    @Test
    void whenGetNewTasks() {
        Category category = new Category(1, "Работа");
        TaskDto task1 = new TaskDto(1, "login", "name1", "desc1", LocalDateTime.now(),
                false, "urgently", Set.of(category.getName()));
        TaskDto task2 = new TaskDto(1, "login", "name2", "desc2", LocalDateTime.now(),
                false, "urgently", Set.of(category.getName()));
        List<TaskDto> taskDtoList = List.of(task1, task2);
        when(taskService.getNewTasks()).thenReturn(taskDtoList);

        Model model = new ConcurrentModel();
        String view = taskController.getNewTasks(model);
        Object tasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(tasks).usingRecursiveComparison().isEqualTo(taskDtoList);
    }

    @Test
    void whenGetTaskByIdIsSuccess() {
        Integer taskId = 1;
        Category category = new Category(1, "Работа");
        TaskDto task1 = new TaskDto(taskId, "login", "name1", "desc1", LocalDateTime.now(),
                false, "urgently", Set.of(category.getName()));
        when(taskService.getById(taskId)).thenReturn(Optional.of(task1));

        Model model = new ConcurrentModel();
        String view = taskController.getTaskById(model, taskId);
        Object task = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/task");
        assertThat(task).usingRecursiveComparison().isEqualTo(task1);
    }

    @Test
    void whenGetTaskByIdIsNotSuccess() {
        Integer nonExistId = 999;
        String expectedMessage = "Задача с указанным идентификатором не найдена";
        when(taskService.getById(nonExistId)).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = taskController.getTaskById(model, nonExistId);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void whenCompleteTaskIsSuccess() {
        Integer taskId = 1;
        when(taskService.completeTask(taskId)).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = taskController.completeTask(model, taskId);

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    void whenCompleteTaskIsNotSuccess() {
        Integer nonExistId = 999;
        String expectedMessage = "Задача с указанным идентификатором не найдена";
        when(taskService.completeTask(nonExistId)).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = taskController.completeTask(model, nonExistId);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void whenDeleteIsSuccess() {
        Integer taskId = 1;
        when(taskService.deleteById(taskId)).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = taskController.delete(model, taskId);

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    void whenDeleteIsNotSuccess() {
        Integer nonExistId = 999;
        String expectedMessage = "Задача с указанным идентификатором не найдена";
        when(taskService.deleteById(nonExistId)).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = taskController.delete(model, nonExistId);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void whenGetUpdatePageIsSuccess() {
        Integer taskId = 1;
        Category category = new Category(1, "Работа");
        TaskDto task1 = new TaskDto(taskId, "login", "name1", "desc1", LocalDateTime.now(),
                false, "urgently", Set.of(category.getName()));
        when(taskService.getById(taskId)).thenReturn(Optional.of(task1));

        Model model = new ConcurrentModel();
        String view = taskController.getUpdatePage(model, taskId);
        Object task = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/update");
        assertThat(task).usingRecursiveComparison().isEqualTo(task1);
    }

    @Test
    void whenGetUpdatePageIsNotSuccess() {
        Integer nonExistId = 999;
        String expectedMessage = "Задача с указанным идентификатором не найдена";
        when(taskService.getById(nonExistId)).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = taskController.getUpdatePage(model, nonExistId);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    void whenUpdateIsSuccess() {
        Integer taskId = 1;
        Category category = new Category(1, "Работа");
        TaskDto task1 = new TaskDto(taskId, "login", "name1", "desc1", LocalDateTime.now(),
                false, "urgently", Set.of(category.getName()));
        when(taskService.update(any(), any())).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = taskController.update(task1, model, List.of(1));

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    @Test
    void whenUpdateIsNotSuccess() {
        Category category = new Category(1, "Работа");
        TaskDto task1 = new TaskDto(999, "login", "name1", "desc1",
                LocalDateTime.now(), false, "urgently", Set.of(category.getName()));
        String expectedMessage = "Задача с указанным идентификатором не найдена";
        when(taskService.update(any(), any())).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = taskController.update(task1, model, List.of(1));
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}