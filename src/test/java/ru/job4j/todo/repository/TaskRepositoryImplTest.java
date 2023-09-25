package ru.job4j.todo.repository;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.model.entity.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.*;

class TaskRepositoryImplTest {

    private static TaskRepository taskRepository;
    private static UserRepository userRepository;
    private User user;

    private final RecursiveComparisonConfiguration recursiveComparisonConfiguration =
            RecursiveComparisonConfiguration.builder()
                    .withEqualsForFields(localDateTimeMatches, "created")
                    .build();

    @BeforeAll
    public static void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);

        taskRepository = new TaskRepositoryImpl(crudRepository);
        userRepository = new UserRepositoryImpl(crudRepository);
    }

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .login("login")
                .password("password")
                .name("name")
                .build();
        userRepository.create(user);
    }

    @AfterEach
    public void deleteTasks() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenAddNewTask() {
        Task savedTask = Task.builder()
                .user(user)
                .name("actual name")
                .description("actual description")
                .created(LocalDateTime.now())
                .build();
        Optional<Task> expectedTask = taskRepository.create(savedTask);
        Integer expectedTaskId = savedTask.getId();

        Task actualTask = taskRepository.findById(expectedTaskId).get();

        assertThat(actualTask)
                .usingRecursiveComparison(recursiveComparisonConfiguration)
                .isEqualTo(expectedTask.get());
    }

    @Test
    public void whenUpdateTaskIsSuccessByValidId() {
        String nameForUpdate = "new name";
        String descriptionForUpdate = "new description";
        LocalDateTime createdForUpdate = LocalDateTime.now().plusDays(2);
        Task oldTask = Task.builder()
                .user(user)
                .name("old name")
                .description("old description")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(oldTask);
        Integer taskId = oldTask.getId();
        Task taskForUpdate = Task.builder()
                .id(taskId)
                .user(user)
                .name(nameForUpdate)
                .description(descriptionForUpdate)
                .created(createdForUpdate)
                .build();

        boolean isUpdated = taskRepository.update(taskForUpdate);
        Task actualTask = taskRepository.findById(taskId).get();

        assertThat(isUpdated).isTrue();
        assertThat(actualTask).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(taskForUpdate);
    }

    @Test
    public void whenUpdateTaskIsNotSuccessByInvalidId() {
        String nameForUpdate = "new name";
        String descriptionForUpdate = "new description";
        LocalDateTime createdForUpdate = LocalDateTime.now().plusDays(2);
        Task oldTask = Task.builder()
                .user(user)
                .description("old description")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(oldTask);
        Integer notValidTaskId = 999;
        Task taskForUpdate = Task.builder()
                .id(notValidTaskId)
                .user(user)
                .name(nameForUpdate)
                .description(descriptionForUpdate)
                .created(createdForUpdate)
                .build();

        boolean isUpdated = taskRepository.update(taskForUpdate);

        assertThat(isUpdated).isFalse();
    }

    @Test
    public void whenDeleteTaskIsSuccessByValidId() {
        Task taskForDelete = Task.builder()
                .name("actual name")
                .user(user)
                .description("actual description")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(taskForDelete);
        Integer expectedTaskId = taskForDelete.getId();

        boolean isDeleted = taskRepository.deleteById(expectedTaskId);
        Optional<Task> deletedTask = taskRepository.findById(expectedTaskId);

        assertThat(isDeleted).isTrue();
        assertThat(deletedTask).isEmpty();
    }

    @Test
    public void whenDeleteTaskIsNotSuccessByInvalidId() {
        Task taskForDelete = Task.builder()
                .name("actual name")
                .user(user)
                .description("actual description")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(taskForDelete);
        Integer invalidId = 9999;

        boolean isDeleted = taskRepository.deleteById(invalidId);
        Optional<Task> task = taskRepository.findById(taskForDelete.getId());

        assertThat(isDeleted).isFalse();
        assertThat(task).isPresent();
    }

    @Test
    public void whenCompleteTask() {
        Task task = Task.builder()
                .name("name")
                .user(user)
                .description("desc")
                .created(LocalDateTime.now())
                .done(false)
                .build();
        taskRepository.create(task);
        Integer taskId = task.getId();

        boolean isComplete = taskRepository.completeTask(taskId);
        boolean actualDone = taskRepository.findById(taskId).get().isDone();

        assertThat(isComplete).isTrue();
        assertThat(actualDone).isTrue();
    }

    @Test
    public void whenDeleteAll() {
        Task task1 = Task.builder()
                .name("name1")
                .user(user)
                .description("description1")
                .created(LocalDateTime.now())
                .build();
        Task task2 = Task.builder()
                .name("name2")
                .user(user)
                .description("description2")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(task1);
        taskRepository.create(task2);
        Integer taskId1 = task1.getId();
        Integer taskId2 = task2.getId();

        taskRepository.deleteAll();
        Optional<Task> actualTask1 = taskRepository.findById(taskId1);
        Optional<Task> actualTask2 = taskRepository.findById(taskId2);

        assertThat(actualTask1).isEmpty();
        assertThat(actualTask2).isEmpty();
    }

    @Test
    public void whenFindByValidId() {
        Task expectedTask = Task.builder()
                .name("actual name")
                .user(user)
                .description("actual description")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(expectedTask);
        Integer taskId = expectedTask.getId();

        Task actualTask = taskRepository.findById(taskId).get();

        assertThat(actualTask).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expectedTask);
    }

    @Test
    public void whenFindByInvalidId() {
        Task expectedTask = Task.builder()
                .user(user)
                .description("actual description")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(expectedTask);
        Integer invalidId = 999;

        Optional<Task> actualTask = taskRepository.findById(invalidId);

        assertThat(actualTask).isEmpty();
    }

    @Test
    public void whenFindAll() {
        Task task1 = Task.builder()
                .name("name1")
                .user(user)
                .description("description1")
                .created(LocalDateTime.now())
                .build();
        Task task2 = Task.builder()
                .name("name2")
                .user(user)
                .description("description2")
                .created(LocalDateTime.now())
                .build();
        taskRepository.create(task1);
        taskRepository.create(task2);
        List<Task> expectedTasks = List.of(task1, task2);

        List<Task> actualTasks = taskRepository.findAll();

        assertTaskList(actualTasks, expectedTasks);
    }

    @Test
    public void whenFindAllCompletedTasks() {
        Task task1 = Task.builder()
                .name("name1")
                .user(user)
                .description("description1")
                .created(LocalDateTime.now())
                .done(true)
                .build();
        Task task2 = Task.builder()
                .name("name2")
                .user(user)
                .description("description2")
                .created(LocalDateTime.now())
                .done(true)
                .build();
        Task task3 = Task.builder()
                .name("name3")
                .user(user)
                .description("description2")
                .created(LocalDateTime.now())
                .done(false)
                .build();
        taskRepository.create(task1);
        taskRepository.create(task2);
        taskRepository.create(task3);
        List<Task> expectedTasks = List.of(task1, task2);

        List<Task> actualTasks = taskRepository.findTasksByDone(true);

        assertTaskList(actualTasks, expectedTasks);
    }

    @Test
    public void whenFindNewTasks() {
        Task task1 = Task.builder()
                .name("name1")
                .user(user)
                .description("description1")
                .created(LocalDateTime.now().minusHours(10))
                .done(false)
                .build();
        Task task2 = Task.builder()
                .name("name2")
                .user(user)
                .description("description2")
                .created(LocalDateTime.now())
                .done(false)
                .build();
        Task task3 = Task.builder()
                .name("name3")
                .user(user)
                .description("description2")
                .created(LocalDateTime.now().minusDays(1))
                .done(true)
                .build();
        taskRepository.create(task1);
        taskRepository.create(task2);
        taskRepository.create(task3);
        List<Task> expectedTasks = List.of(task1, task2);

        List<Task> actualTasks = taskRepository.findTasksByDone(false);

        assertTaskList(actualTasks, expectedTasks);
    }

    private static final BiPredicate<LocalDateTime, LocalDateTime> localDateTimeMatches = (first, second) ->
            first.truncatedTo(ChronoUnit.SECONDS).isEqual(second.truncatedTo(ChronoUnit.SECONDS));

    private void assertTaskList(List<Task> actual, List<Task> expected) {
        assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
    }
}