package com.glaxier.taskmanagerapi.repository;

import com.glaxier.taskmanagerapi.model.User;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepositoryCustom {
    @Query("{ 'email' : ?0 }")
    Optional<User> findByEmail(String email);
}
