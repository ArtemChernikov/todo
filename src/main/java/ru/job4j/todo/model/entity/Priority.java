package ru.job4j.todo.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.10.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "priorities")
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private Integer position;
}
