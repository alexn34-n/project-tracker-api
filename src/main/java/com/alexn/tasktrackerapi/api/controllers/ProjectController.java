package com.alexn.tasktrackerapi.api.controllers;

import com.alexn.tasktrackerapi.api.dto.AckDto;
import com.alexn.tasktrackerapi.api.dto.ProjectDto;
import com.alexn.tasktrackerapi.api.exceptions.BadRequestException;
import com.alexn.tasktrackerapi.api.factories.ProjectDtoFactory;
import com.alexn.tasktrackerapi.store.entities.ProjectEntity;
import com.alexn.tasktrackerapi.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectRepository projectRepository;

    ProjectDtoFactory projectDtoFactory;


    public static final String FETCH_PROJECTS = "/api/projects";
    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";



    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAll);
        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());

    }


    @PutMapping(CREATE_OR_UPDATE_PROJECT )
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id", required = false)Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName
            ){

        optionalProjectName=optionalProjectName.filter(projectName->!projectName.trim().isEmpty());

        boolean isCreate =!optionalProjectId.isPresent();

        if(isCreate && !optionalProjectName.isPresent()){
            throw  new BadRequestException("Project name  can't be empty.");
        }

      final   ProjectEntity project= optionalProjectId
                .map(this::getProjectOrThrowException)
                .orElseGet(()-> ProjectEntity.builder().build());



        optionalProjectName
                .ifPresent(projectName->{

                    projectRepository
                            .findByName(projectName)
                            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(
                                        String.format("Project \"%s\" already exists.", projectName)
                                );
                            });
                    project.setName(projectName);
                });

        final ProjectEntity saveProject =projectRepository.saveAndFlush(project);


        return projectDtoFactory.makeProjectDto(project);

    }

       @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId) {

       getProjectOrThrowException(projectId);

       projectRepository.deleteById(projectId);

       return AckDto.makeDefault(true);
    }

    private ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new BadRequestException(
                                String.format(
                                        "Project with \"%s\" doesn't exists.",
                                        projectId
                                )
                        )
                );
    }

}
