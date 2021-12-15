package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.model.LoginForm;
import com.glaxier.taskmanagerapi.model.User;
import com.glaxier.taskmanagerapi.service.PartialUpdate;
import com.glaxier.taskmanagerapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class UsersController {

    UserService userService;
    PartialUpdate partialUpdate;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/users/registration")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<User> login(@Valid @RequestBody LoginForm loginForm) {

            Optional<User> userData = userService.findByEmail(loginForm.getEmail());
            if (userData.isPresent()) {
                if (passwordEncoder.matches(loginForm.getPassword(), userData.get().getPassword())) {
                    return new ResponseEntity<>(userData.get(), HttpStatus.OK);
                }
            }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        Optional<User> userData = userService.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody @Valid Map<Object, Object> user) {
        Optional<User> userData = userService.findById(id);

        if (userData.isPresent()) {
            userData = partialUpdate.userPartialUpdate(user,  userData);
            return new ResponseEntity<>(userService.save(userData.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteUsers() {
        userService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
