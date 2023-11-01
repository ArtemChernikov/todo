package ru.job4j.todo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 03.07.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Integer id;
    private String userLogin;
    private String name;
    private String description;
    private LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC"));
    private boolean done = false;
    private String priorityName;
    private Set<String> categoryNames = new HashSet<>();

}
