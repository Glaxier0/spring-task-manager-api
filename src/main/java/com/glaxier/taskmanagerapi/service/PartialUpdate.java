package com.glaxier.taskmanagerapi.service;


import com.glaxier.taskmanagerapi.model.Task;
import com.glaxier.taskmanagerapi.model.Users;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PartialUpdate {

    private Validator validator;

    public Optional<Users> userPartialUpdate(Map<Object, Object> user, Optional<Users> userData) {
        user.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Users.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, userData.get(), value);
        });
        validateUser(userData.get());
        return userData;
    }

    public Optional<Task> taskPartialUpdate(Map<Object, Object> task, Optional<Task> taskData) {
        task.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Task.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, taskData.get(), value);
        });
        validateTask(taskData.get());
        return taskData;
    }

    public void validateUser(Users user) {
        Set<ConstraintViolation<Users>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void validateTask(Task task) {
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
