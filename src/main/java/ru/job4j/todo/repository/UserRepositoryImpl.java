package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.entity.User;

import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.09.2023
 */
@Slf4j
@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> create(User user) {
        Session session = sessionFactory.openSession();
        Optional<User> savedUser = Optional.empty();

        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            savedUser = Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return savedUser;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sessionFactory.openSession();
        Optional<User> user = Optional.empty();

        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User WHERE login = :login and password = :password", User.class);
            query.setParameter("login", login);
            query.setParameter("password", password);
            user = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return user;
    }
}
