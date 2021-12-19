package com.glaxier.taskmanagerapi.service;

import com.glaxier.taskmanagerapi.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<Users> findAll();
    Optional<Users> findById(String id);
    Optional<Users> findByEmail(String email);
    void deleteAll();
    Users save(Users user);
    void delete(Users user);
    void deleteById(String id);
}
