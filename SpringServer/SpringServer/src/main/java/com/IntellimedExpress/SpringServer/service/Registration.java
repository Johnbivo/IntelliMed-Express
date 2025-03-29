package com.IntellimedExpress.SpringServer.service;

import com.IntellimedExpress.SpringServer.entity.User;
import com.IntellimedExpress.SpringServer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Registration {

    private final UserRepository userRepository;

    public Registration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, Object> registerUser(String username, String password, String email,
                                            String firstName, String lastName, String phoneNumber, String address) {

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user using constructor instead of builder
        User user = new User(username, password, email, firstName, lastName, phoneNumber, address);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        // Generate a simple session token
        String token = UUID.randomUUID().toString();

        // Return user info and token
        Map<String, Object> response = new HashMap<>();
        response.put("userId", savedUser.getId());
        response.put("username", savedUser.getUsername());
        response.put("token", token);
        response.put("message", "User registered successfully!");

        return response;
    }
}