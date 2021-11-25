package com.glaxier.taskmanagerapi;

import com.glaxier.taskmanagerapi.repository.UserRepository;
import com.glaxier.taskmanagerapi.model.User;
import org.springframework.context.annotation.Bean;


public class test {

    UserRepository userRepository;

    @Bean
    public void repoTest() {
        userRepository.save(new User("Tahir", "tahir2001@hotmail.com", "1234567", 20));
    }
}
