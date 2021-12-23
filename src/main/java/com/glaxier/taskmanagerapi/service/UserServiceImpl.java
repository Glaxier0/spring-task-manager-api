package com.glaxier.taskmanagerapi.service;

import com.glaxier.taskmanagerapi.model.Users;
import com.glaxier.taskmanagerapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public Users save(Users user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Users user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

}
