package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.06.2023
 */
@AllArgsConstructor
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Task> create(Task task) {
        Session session = sessionFactory.openSession();
        Optional<Task> savedTask = Optional.empty();

        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            savedTask = Optional.of(task);
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return savedTask;
    }

    @Override
    public boolean update(Task newTask) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.update(newTask);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer taskId) {
        Session session = sessionFactory.openSession();
        boolean isDeleted = false;

        try {
            session.beginTransaction();
            int effectedRows = session.createQuery("DELETE Task WHERE id = :id")
                    .setParameter("id", taskId)
                    .executeUpdate();
            session.getTransaction().commit();
            isDeleted = effectedRows > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return isDeleted;
    }

    @Override
    public void deleteAll() {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Task")
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Task> findById(Integer taskId) {
        Session session = sessionFactory.openSession();
        Optional<Task> task = Optional.empty();

        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("FROM Task WHERE id = :id", Task.class)
                    .setParameter("id", taskId);
            task = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public List<Task> findAll() {
        Session session = sessionFactory.openSession();
        List<Task> tasks = new ArrayList<>();

        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("FROM Task", Task.class);
            tasks = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    @Override
    public List<Task> findAllCompletedTasks() {
        Session session = sessionFactory.openSession();
        List<Task> tasks = new ArrayList<>();

        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("FROM Task WHERE done = true", Task.class);
            tasks = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    @Override
    public List<Task> findNewTasks() {
        Session session = sessionFactory.openSession();
        List<Task> tasks = new ArrayList<>();

        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("FROM Task WHERE created > current_date", Task.class);
            tasks = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }
}
