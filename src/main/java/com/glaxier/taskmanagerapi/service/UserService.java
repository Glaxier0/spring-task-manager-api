package com.glaxier.taskmanagerapi.service;

import com.glaxier.taskmanagerapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    void deleteAll();
    User save(User user);
    void delete(User user);
    void deleteById(String id);
}
