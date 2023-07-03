package ru.job4j.todo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 03.07.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    Integer id;

    String description;

    LocalDateTime created;

    boolean done;
}
