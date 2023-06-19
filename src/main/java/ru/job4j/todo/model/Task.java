package ru.job4j.todo.model;

import lombok.Data;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.06.2023
 */
@Entity
@Data
public class Task {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "description")
    String description;
    @Column(name = "created", nullable = false)
    LocalDateTime created;
    @Column(name = "done", nullable = false, columnDefinition = "boolean default false")
    boolean done;
}
