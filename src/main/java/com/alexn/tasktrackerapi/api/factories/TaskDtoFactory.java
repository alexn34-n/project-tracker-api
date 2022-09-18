package com.alexn.tasktrackerapi.api.factories;

import com.alexn.tasktrackerapi.api.dto.TaskDto;
import com.alexn.tasktrackerapi.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity entity) {

        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();
    }
}
