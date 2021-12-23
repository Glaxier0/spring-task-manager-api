package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.Util.JwtUtils;
import com.glaxier.taskmanagerapi.model.JwtResponse;
import com.glaxier.taskmanagerapi.model.LoginForm;
import com.glaxier.taskmanagerapi.model.ProfileResponse;
import com.glaxier.taskmanagerapi.model.Users;
import com.glaxier.taskmanagerapi.security.AuthTokenFilter;
import com.glaxier.taskmanagerapi.service.PartialUpdate;
import com.glaxier.taskmanagerapi.service.UserDetailsImpl;
import com.glaxier.taskmanagerapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    AuthTokenFilter authTokenFilter;

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
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        Users user = userService.findByEmail(loginForm.getEmail()).get();
        user.getTokens().add(jwt);
        userService.save(user);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new ResponseEntity<>(
                new JwtResponse(userDetails.getId(), userDetails.getUsername(),
                        userDetails.getEmail(), userDetails.getAge(), jwt), HttpStatus.OK);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader HttpHeaders httpHeaders) {
        String token = jwtUtils.getToken(httpHeaders);
        Users user = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get();
        user.getTokens().remove(token);
        userService.save(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/users/logoutAll")
    public ResponseEntity<?> logoutAll(@RequestHeader HttpHeaders httpHeaders) {
        String token = jwtUtils.getToken(httpHeaders);
        Users user = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get();
        user.setTokens(new ArrayList<>());
        userService.save(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<Users> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> getProfile(@RequestHeader HttpHeaders httpHeaders) {
        String token = jwtUtils.getToken(httpHeaders);
        Users user = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get();

        return new ResponseEntity<>(new ProfileResponse(user.getId(), user.getName(),
                user.getEmail(), user.getAge()), HttpStatus.OK);
    }

    @PatchMapping("/users/me")
    public ResponseEntity<?> updateProfile(@RequestHeader HttpHeaders httpHeaders
            , @RequestBody @Valid Map<Object, Object> user) {
        String token = jwtUtils.getToken(httpHeaders);
        Optional<Users> userData = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token));
        if (userData.isPresent()) {
            userData = partialUpdate.userPartialUpdate(user, userData);
            userService.save(userData.get());
            return new ResponseEntity<>(new ProfileResponse(userData.get().getId(), userData.get().getName(),
                    userData.get().getEmail(), userData.get().getAge()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteUsers() {
        userService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<HttpStatus> deleteProfile(@RequestHeader HttpHeaders httpHeaders) {
        String token = jwtUtils.getToken(httpHeaders);
        userService.delete(userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
