package com.alexn.tasktrackerapi.store.repositories;

import com.alexn.tasktrackerapi.store.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {

    Optional<ProjectEntity> findByName(String name);

    Stream<ProjectEntity> streamAll();

    Stream<ProjectEntity> streamAllByNameStartsWithIgnoreCase(String prefixName);
}
