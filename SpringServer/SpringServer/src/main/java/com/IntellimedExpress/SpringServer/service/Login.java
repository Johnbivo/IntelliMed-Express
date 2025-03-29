package com.IntellimedExpress.SpringServer.service;

import com.IntellimedExpress.SpringServer.entity.User;
import com.IntellimedExpress.SpringServer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Login {

    private final UserRepository userRepository;

    public Login(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, Object> login(String username, String password) {
        // Find user in database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check password
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        // Generate a simple session token
        String token = UUID.randomUUID().toString();

        // Return response
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("token", token);
        response.put("message", "User logged in successfully!");

        return response;
    }

    // Simple logout method that doesn't require token
    public Map<String, Object> logout(String username) {
        // We could add additional logic here if needed
        // For example, you might want to invalidate any server-side session data

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User " + username + " logged out successfully");
        return response;
    }
}