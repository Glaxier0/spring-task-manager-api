package com.glaxier.taskmanagerapi.service;

import com.glaxier.taskmanagerapi.model.Task;
import com.glaxier.taskmanagerapi.model.User;

import java.util.List;

public interface TaskService {
    List<Task> findAll();
    void deleteAll();
    Task save(Task task);
    void delete(Task task);
}
