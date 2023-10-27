package ru.job4j.todo.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Priority;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.model.entity.User;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", source = "task.id")
    @Mapping(target = "userLogin", source = "task.user.login")
    @Mapping(target = "name", source = "task.name")
    @Mapping(target = "description", source = "task.description")
    @Mapping(target = "created", source = "task.created")
    @Mapping(target = "done", source = "task.done")
    @Mapping(target = "priorityName", source = "task.priority.name")
    @Mapping(target = "categoryNames", source = "categoryNames")
    TaskDto taskToTaskDto(Task task, Set<String> categoryNames);

    @Mapping(target = "id", source = "taskDto.id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "name", source = "taskDto.name")
    @Mapping(target = "description", source = "taskDto.description")
    @Mapping(target = "created", source = "taskDto.created")
    @Mapping(target = "done", source = "taskDto.done")
    @Mapping(target = "priority", source = "priority")
    Task taskDtoToTask(TaskDto taskDto, User user, Priority priority);

    List<TaskDto> taskListToTaskDtoList(List<Task> taskList);
}
