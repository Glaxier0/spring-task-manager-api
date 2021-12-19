package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.Util.JwtUtil;
import com.glaxier.taskmanagerapi.model.LoginForm;
import com.glaxier.taskmanagerapi.model.Users;
import com.glaxier.taskmanagerapi.service.PartialUpdate;
import com.glaxier.taskmanagerapi.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final UserServiceImpl userServiceImpl;
    private final PartialUpdate partialUpdate;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtToken;
    private AuthenticationManager authenticationManager;

    @PostMapping("/users/registration")
    public ResponseEntity<Users> saveUser(@Valid @RequestBody Users user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return new ResponseEntity<>(userServiceImpl.save(user), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginForm.getEmail(),
                            loginForm.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        UserDetails userDetails = userServiceImpl.loadUserByUsername(loginForm.getEmail());
        Users user = userServiceImpl.findByEmail(loginForm.getEmail()).get();
        final String jwt = jwtToken.generateToken(userDetails);
        user.setToken(jwt);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<Users> getUsers() {
        return userServiceImpl.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable("id") String id) {
        Optional<Users> userData = userServiceImpl.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") String id, @RequestBody @Valid Map<Object, Object> user) {
        Optional<Users> userData = userServiceImpl.findById(id);

        if (userData.isPresent()) {
            userData = partialUpdate.userPartialUpdate(user, userData);
            return new ResponseEntity<>(userServiceImpl.save(userData.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteUsers() {
        userServiceImpl.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String id) {
        try {
            userServiceImpl.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
