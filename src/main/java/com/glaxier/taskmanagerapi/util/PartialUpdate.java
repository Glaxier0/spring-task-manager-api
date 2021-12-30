package com.glaxier.taskmanagerapi.util;

import com.glaxier.taskmanagerapi.model.Task;
import com.glaxier.taskmanagerapi.model.Users;
import com.glaxier.taskmanagerapi.model.pojo.UpdateTask;
import com.glaxier.taskmanagerapi.model.pojo.UpdateUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class PartialUpdate {

    private Validator validator;

    public Optional<Users> userPartialUpdate(UpdateUser user, Optional<Users> userData) {
        userData.get().updateUser(user);
        validateUser(userData.get());
        return userData;
    }

    public Optional<Task> taskPartialUpdate(UpdateTask task, Optional<Task> taskData) {
        taskData.get().updateTask(task);
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
