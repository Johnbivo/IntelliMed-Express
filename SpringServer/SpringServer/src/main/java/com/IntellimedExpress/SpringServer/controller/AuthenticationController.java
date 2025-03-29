package com.IntellimedExpress.SpringServer.controller;

import com.IntellimedExpress.SpringServer.service.Login;
import com.IntellimedExpress.SpringServer.service.Registration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final Login login;
    private final Registration registration;

    public AuthenticationController(Login loginService, Registration registrationService) {
        this.login = loginService;
        this.registration = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        try {
            Map<String, Object> response = registration.registerUser(
                    request.get("username"),
                    request.get("password"),
                    request.get("email"),
                    request.get("firstName"),
                    request.get("lastName"),
                    request.get("phoneNumber"),
                    request.get("address")
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Registration failed: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        try {
            Map<String, Object> response = login.login(
                    request.get("username"),
                    request.get("password")
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        if (username == null || username.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Username is required for logout");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = login.logout(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}