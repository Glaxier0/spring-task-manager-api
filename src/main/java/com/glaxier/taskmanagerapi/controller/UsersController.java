package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.Util.JwtUtils;
import com.glaxier.taskmanagerapi.model.JwtResponse;
import com.glaxier.taskmanagerapi.model.LoginForm;
import com.glaxier.taskmanagerapi.model.Users;
import com.glaxier.taskmanagerapi.service.PartialUpdate;
import com.glaxier.taskmanagerapi.service.UserDetailsImpl;
import com.glaxier.taskmanagerapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@AllArgsConstructor
public class UsersController {

    AuthenticationManager authenticationManager;
    UserService userService;
    PartialUpdate partialUpdate;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    @PostMapping("/users/registration")
    public ResponseEntity<Users> saveUser(@Valid @RequestBody Users user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getEmail(),
                        loginForm.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        Users user = userService.findByEmail(loginForm.getEmail()).get();
        user.setToken(jwt);
        userService.save(user);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(
                new JwtResponse(userDetails.getId(), userDetails.getUsername(),
                        userDetails.getEmail(), userDetails.getAge(), jwt), HttpStatus.OK);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<?> logout() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userService.findByEmail(userDetails.getEmail()).get();
        user.setToken(null);
        userService.save(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/users/logoutAll")

    @GetMapping("/users")
    public List<Users> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable("id") String id) {
        Optional<Users> userData = userService.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") String id, @RequestBody @Valid Map<Object, Object> user) {
        Optional<Users> userData = userService.findById(id);

        if (userData.isPresent()) {
            userData = partialUpdate.userPartialUpdate(user, userData);
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
