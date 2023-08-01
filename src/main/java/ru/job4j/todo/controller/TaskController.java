package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 13.07.2023
 */
@Controller
@AllArgsConstructor
@RequestMapping
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String getTasks(Model model) {
        List<TaskDto> tasks = taskService.getAll();
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/completed")
    public String getCompletedTasks(Model model) {
        List<TaskDto> tasks = taskService.getAllCompletedTasks();
        model.addAttribute("completedTasks", tasks);
        return "tasks/completed";
    }

    @GetMapping("/new")
    public String getNewTasks(Model model) {
        List<TaskDto> tasks = taskService.getNewTasks();
        model.addAttribute("newTasks", tasks);
        return "tasks/new";
    }

    @GetMapping("/task/{id}")
    public String getTaskById(Model model, @PathVariable Integer id) {
        Optional<TaskDto> optionalTaskDto = taskService.getById(id);
        if (optionalTaskDto.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("task", optionalTaskDto.get());
        return "tasks/task";
    }

    @GetMapping("/task/complete/{id}")
    public String completeTask(Model model, @PathVariable Integer id) {
        var isCompleted = taskService.completeTask(id);
        if (!isCompleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/";
    }

    @GetMapping("/task/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/";
    }

    @GetMapping("/task/update/{id}")
    public String getUpdatePage(Model model, @PathVariable Integer id) {
        Optional<TaskDto> optionalTaskDto = taskService.getById(id);
        if (optionalTaskDto.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("task", optionalTaskDto.get());
        return "tasks/update";
    }

    @PostMapping("/task/update/{id}")
    public String update(@ModelAttribute Task task, Model model) {
        var isUpdate = taskService.update(task);
        if (!isUpdate) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/";
    }

}
