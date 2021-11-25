package com.glaxier.taskmanagerapi.service;


import com.glaxier.taskmanagerapi.model.Task;
import com.glaxier.taskmanagerapi.model.User;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Service
public class PartialUpdate {

    public Optional<User> userPartialUpdate(Map<Object, Object> user, Optional<User> userData) {
        user.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, (String) key);
            field.setAccessible(true);
            if (field.toString().endsWith("password") && value.toString().length() < 8) {
                throw new Error("Password must be over 8 characters.");
            }
            ReflectionUtils.setField(field, userData.get(), value);
        });
        return userData;
    }

    public Optional<Task> taskPartialUpdate(Map<Object, Object> task, Optional<Task> taskData) {
        task.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Task.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, taskData.get(), value);
        });
        return taskData;
    }
}
