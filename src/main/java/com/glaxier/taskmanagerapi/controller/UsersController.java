package com.glaxier.taskmanagerapi.controller;

import com.glaxier.taskmanagerapi.Util.ImageResizer;
import com.glaxier.taskmanagerapi.Util.JwtUtils;
import com.glaxier.taskmanagerapi.model.*;
import com.glaxier.taskmanagerapi.security.AuthTokenFilter;
import com.glaxier.taskmanagerapi.service.PartialUpdate;
import com.glaxier.taskmanagerapi.service.UserDetailsImpl;
import com.glaxier.taskmanagerapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
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
    ImageResizer imageResizer;

    @PostMapping("/users/registration")
    public ResponseEntity<?> saveUser(@Valid @RequestBody Users user) {
        Optional<Users> usersData = userService.findByEmail(user.getEmail());
        if (usersData.isPresent()) {
            throw new DuplicateKeyException("Email already in use!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(new ProfileResponse(user.getId(), user.getName(),
                user.getEmail(), user.getAge()), HttpStatus.CREATED);
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

    @PostMapping(value = "/users/me/avatar", consumes = {})
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatar,
                                          @RequestHeader HttpHeaders httpHeaders) throws NoSuchFileException, HttpMediaTypeNotSupportedException {
        if (avatar.isEmpty()) {
            throw new NoSuchFileException("Please upload image with jpg, jpeg or png extensions.");
        }
        if (!avatar.getContentType().substring(6).matches("jpg|png|jpeg")) {
            throw new HttpMediaTypeNotSupportedException("Please upload image with jpg, jpeg or png extensions.");
        }
        System.out.println(avatar.getContentType());
        String token = jwtUtils.getToken(httpHeaders);
        Users user = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token)).get();

        String resizedImageByteArray = null;
        try {
            resizedImageByteArray = imageResizer.resizeImage(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setAvatar(resizedImageByteArray);
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
            , @RequestBody @Valid UpdateUser user) {
        String token = jwtUtils.getToken(httpHeaders);
        Optional<Users> userData = userService.findByEmail(jwtUtils.getUserNameFromJwtToken(token));
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
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
