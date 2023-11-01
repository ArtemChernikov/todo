package ru.job4j.todo.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.09.2023
 */
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_zone", nullable = false)
    private String timezone;

}
