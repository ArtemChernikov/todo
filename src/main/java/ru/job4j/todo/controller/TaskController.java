package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.service.TaskService;

import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 13.07.2023
 */
@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String getTasks(Model model) {
        List<TaskDto> tasks = taskService.getAll();
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

}
