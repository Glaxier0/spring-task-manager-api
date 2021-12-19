package com.glaxier.taskmanagerapi.service;

import com.glaxier.taskmanagerapi.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();
    Optional<Task> findById(String id);
    void deleteAll();
    Task save(Task task);
    void deleteById(String id);
}
