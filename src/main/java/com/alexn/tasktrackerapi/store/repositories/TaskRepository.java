package com.alexn.tasktrackerapi.store.repositories;

import com.alexn.tasktrackerapi.store.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
}
