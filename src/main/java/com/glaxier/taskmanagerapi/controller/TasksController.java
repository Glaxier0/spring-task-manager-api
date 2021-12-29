package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.Util.JwtUtils;
import com.glaxier.taskmanagerapi.model.Task;
import com.glaxier.taskmanagerapi.service.PartialUpdate;
import com.glaxier.taskmanagerapi.service.TaskService;
import com.glaxier.taskmanagerapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class TasksController {
    TaskService taskService;
    PartialUpdate partialUpdate;
    JwtUtils jwtUtils;
    UserService userService;

    @PostMapping("/tasks")
    public ResponseEntity<Task> saveTask(@RequestBody Task task, @RequestHeader HttpHeaders httpHeaders) {
        String token = jwtUtils.getToken(httpHeaders);
        String userId = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get().getId();
        System.out.println(userId);
        task.setUserId(userId);
        try {
            return new ResponseEntity<>(taskService.save(task), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") String id) {
        Optional<Task> taskData = taskService.findById(id);
        if (taskData.isPresent()) {
            return new ResponseEntity<>(taskData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") String id, @RequestBody Map<Object, Object> task) {
        Optional<Task> taskData = taskService.findById(id);

        if (taskData.isPresent()) {
            taskData = partialUpdate.taskPartialUpdate(task, taskData);
            return new ResponseEntity<>(taskService.save(taskData.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/tasks")
    public ResponseEntity<HttpStatus> deleteTasks() {
        taskService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") String id) {
        try {
            taskService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
