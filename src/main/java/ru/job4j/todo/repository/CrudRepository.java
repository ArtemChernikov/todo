package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 24.09.2023
 */
@Slf4j
@AllArgsConstructor
@Component
public class CrudRepository {
    private final SessionFactory sessionFactory;

    public void run(Consumer<Session> command) {
        tx(session -> {
                    command.accept(session);
                    return null;
                }
        );
    }

    public void run(String query) {
        Consumer<Session> command = session -> {
            Query sql = session.createQuery(query);
            sql.executeUpdate();
        };
        run(command);
    }

    public Boolean bool(String query, Map<String, Object> parameters) {
        Function<Session, Boolean> command = session -> {
            Query sql = session.createQuery(query);
            setParameters(sql, parameters);
            int effectedRows = sql.executeUpdate();
            return effectedRows > 0;
        };
        return tx(command);
    }

    public <T> Optional<T> optional(String query, Class<T> tClass, Map<String, Object> parameters) {
        Function<Session, Optional<T>> command = session -> {
            Query<T> sql = session.createQuery(query, tClass);
            setParameters(sql, parameters);
            return sql.uniqueResultOptional();
        };
        return tx(command);
    }

    public <T> List<T> list(String query, Class<T> tClass) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, tClass)
                .list();
        return tx(command);
    }

    public <T> List<T> list(String query, Class<T> tClass, Map<String, Object> parameters) {
        Function<Session, List<T>> command = session -> {
            Query<T> sql = session.createQuery(query, tClass);
            setParameters(sql, parameters);
            return sql.list();
        };
        return tx(command);
    }

    public <T> List<T> list(String query, List<Integer> idList, Class<T> tClass) {
        Function<Session, List<T>> command = session -> {
            Query<T> sql = session.createQuery(query, tClass);
            sql.setParameterList("idList", idList);
            return sql.list();
        };
        return tx(command);
    }

    private <T> T tx(Function<Session, T> command) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    private <T> void setParameters(Query<T> sql, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            sql.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
