package com.glaxier.taskmanagerapi.repository;

import com.glaxier.taskmanagerapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom{

}
