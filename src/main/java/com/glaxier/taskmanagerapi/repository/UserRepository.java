package com.glaxier.taskmanagerapi.repository;

import com.glaxier.taskmanagerapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

}
