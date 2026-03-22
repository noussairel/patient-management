package com.pm.authservice.service;

import com.pm.authservice.model.User;
import org.springframework.stereotype.Service;
import com.pm.authservice.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    public final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
