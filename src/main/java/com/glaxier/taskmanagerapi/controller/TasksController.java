package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.model.Task;
import com.glaxier.taskmanagerapi.model.pojo.UpdateTask;
import com.glaxier.taskmanagerapi.service.TaskService;
import com.glaxier.taskmanagerapi.service.UserService;
import com.glaxier.taskmanagerapi.util.JwtUtils;
import com.glaxier.taskmanagerapi.util.PartialUpdate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class TasksController {
    TaskService taskService;
    PartialUpdate partialUpdate;
    JwtUtils jwtUtils;
    UserService userService;

    @PostMapping("/tasks")
    public ResponseEntity<Task> saveTask(@RequestBody @Valid Task task, @RequestHeader HttpHeaders httpHeaders) {
        String token = jwtUtils.getToken(httpHeaders);
        String userId = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get().getId();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        task.setUserId(userId);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return new ResponseEntity<>(taskService.save(task), HttpStatus.CREATED);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(@RequestHeader HttpHeaders httpHeaders,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        String token = jwtUtils.getToken(httpHeaders);
        String userId = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get().getId();
        List<Task> tasks;
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPages;
        taskPages = taskService.findByUserId(userId, pageable);
        tasks = taskPages.getContent();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
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
    public ResponseEntity<Task> updateTask(@PathVariable("id") String id, @RequestBody @Valid UpdateTask task) {
        Optional<Task> taskData = taskService.findById(id);

        if (taskData.isPresent()) {
            taskData = partialUpdate.taskPartialUpdate(task, taskData);
            taskData.get().setUpdatedAt(ZonedDateTime.now(ZoneOffset.UTC));
            return new ResponseEntity<>(taskService.save(taskData.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
