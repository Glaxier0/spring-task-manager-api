package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.model.User;
import com.glaxier.taskmanagerapi.repository.UserRepository;
import com.glaxier.taskmanagerapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class UsersController {

    UserService userService;
    UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUsers(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateTutorial(@PathVariable("id") String id, @RequestBody User user) {
        Optional<User> userData = userService.findById(id);

        if (userData.isPresent()) {
            userData.get().setName(user.getName());
            userData.get().setEmail(user.getEmail());
            userData.get().setPassword(user.getPassword());
            userData.get().setAge(user.getAge());
            return new ResponseEntity<>(userService.save(userData.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        Optional<User> userData = userService.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users")
    public String deleteUsers() {
        userService.deleteAll();
        return "All users deleted.";
    }

}
