package com.glaxier.taskmanagerapi.repository;

import com.glaxier.taskmanagerapi.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String>, UserRepositoryCustom {

}
