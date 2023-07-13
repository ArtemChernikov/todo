package ru.job4j.todo.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.model.dto.TaskDto;
import ru.job4j.todo.model.entity.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "done", source = "done")
    TaskDto taskToTaskDto(Task task);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "done", source = "done")
    Task taskDtoToTask(TaskDto taskDto);

    List<Task> taskDtoListToTaskList(List<TaskDto> taskDtoList);

    List<TaskDto> taskListToTaskDtoList(List<Task> taskList);
}
