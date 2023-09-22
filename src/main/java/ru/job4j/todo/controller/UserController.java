package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.entity.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.09.2023
 */
@AllArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/registration";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User postUser) {
        var newUser = userService.save(postUser);
        if (newUser.isEmpty()) {
            model.addAttribute("error", "Пользователь с данным логином уже существует");
            return "users/registration";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User postUser, Model model, HttpServletRequest request) {
        var userOptional = userService.getByLoginAndPassword(postUser.getLogin(), postUser.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Логин или пароль введены неверно");
            return "users/login";
        }
        var newSession = request.getSession();
        newSession.setAttribute("user", userOptional.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
