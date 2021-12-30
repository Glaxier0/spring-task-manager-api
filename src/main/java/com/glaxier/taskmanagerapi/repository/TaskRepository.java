package com.glaxier.taskmanagerapi.repository;

import com.glaxier.taskmanagerapi.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
    Page<Task> findByUserId(String userId, Pageable pageable);
}
