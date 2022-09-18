package com.alexn.tasktrackerapi.api.factories;

import com.alexn.tasktrackerapi.api.dto.ProjectDto;
import com.alexn.tasktrackerapi.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity entity) {

        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}