package com.glaxier.taskmanagerapi.service;

import com.glaxier.taskmanagerapi.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();

    Page<Task> findByUserId(String userId, Pageable pageable);

    Optional<Task> findById(String id);

    void deleteAll();

    Task save(Task task);

    void deleteById(String id);
}
