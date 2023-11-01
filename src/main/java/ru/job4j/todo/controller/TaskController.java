package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Category;
import ru.job4j.todo.model.entity.Priority;
import ru.job4j.todo.model.entity.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

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
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping
    public String getTasks(Model model, HttpServletRequest request) {
        String timezone = getUserTimezone(request);
        List<TaskDto> tasks = taskService.getAll(timezone);
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        List<Priority> priorities = priorityService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("priorities", priorities);
        model.addAttribute("categories", categories);
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute TaskDto taskDto, @RequestParam("selectedCategories") List<Integer> categoriesIds) {
        taskService.save(taskDto, categoriesIds);
        return "redirect:/tasks";
    }

    @GetMapping("/completed")
    public String getCompletedTasks(Model model, HttpServletRequest request) {
        String timezone = getUserTimezone(request);
        List<TaskDto> tasks = taskService.getAllCompletedTasks(timezone);
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNewTasks(Model model, HttpServletRequest request) {
        String timezone = getUserTimezone(request);
        List<TaskDto> tasks = taskService.getNewTasks(timezone);
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getTaskById(Model model, @PathVariable Integer id) {
        Optional<TaskDto> optionalTaskDto = taskService.getById(id);
        if (optionalTaskDto.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("task", optionalTaskDto.get());
        return "tasks/task";
    }

    @GetMapping("/complete/{id}")
    public String completeTask(Model model, @PathVariable Integer id) {
        var isCompleted = taskService.completeTask(id);
        if (!isCompleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable Integer id) {
        Optional<TaskDto> optionalTaskDto = taskService.getById(id);
        if (optionalTaskDto.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        List<Priority> priorities = priorityService.findAll();
        model.addAttribute("task", optionalTaskDto.get());
        model.addAttribute("priorities", priorities);
        return "tasks/update";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute TaskDto taskDto, Model model,
                         @RequestParam("selectedCategories") List<Integer> categoriesIds) {
        var isUpdate = taskService.update(taskDto, categoriesIds);
        if (!isUpdate) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    private String getUserTimezone(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return user.getTimezone();
    }

}
