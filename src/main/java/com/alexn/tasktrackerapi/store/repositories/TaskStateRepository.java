package com.alexn.tasktrackerapi.store.repositories;

import com.alexn.tasktrackerapi.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TaskStateRepository extends JpaRepository<TaskStateEntity,Long> {
}
