package ru.job4j.todo.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Task;
import ru.job4j.todo.model.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userLogin", source = "task.user.login")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "done", source = "done")
    TaskDto taskToTaskDto(Task task);

    @Mapping(target = "id", source = "taskDto.id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "name", source = "taskDto.name")
    @Mapping(target = "description", source = "taskDto.description")
    @Mapping(target = "created", source = "taskDto.created")
    @Mapping(target = "done", source = "taskDto.done")
    Task taskDtoToTask(TaskDto taskDto, User user);

    List<TaskDto> taskListToTaskDtoList(List<Task> taskList);
}
