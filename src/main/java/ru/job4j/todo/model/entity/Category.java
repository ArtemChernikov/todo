package ru.job4j.todo.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 23.10.2023
 */
@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
}
