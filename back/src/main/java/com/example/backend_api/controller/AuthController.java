package com.example.backend_api.controller;

import com.example.backend_api.dto.CreateAccountRequest;
import com.example.backend_api.dto.LoginRequest;
import com.example.backend_api.dto.TokenResponse;
import com.example.backend_api.exception.InvalidCredentialsException;
import com.example.backend_api.exception.UserAlreadyExistsException;
import com.example.backend_api.model.User;
import com.example.backend_api.security.JwtUtil;
import com.example.backend_api.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // POST /account
    @PostMapping("/account")
    public ResponseEntity<Map<String, String>> createAccount(@RequestBody CreateAccountRequest request) {
        // Check existing user with email
        if (userService.getById(request.getEmail()) != null) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userService.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Account created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // POST /token
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        User user = userService.getById(request.getEmail());
        
        if (user == null || !userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getEmail());
        
        return ResponseEntity.ok(new TokenResponse(token));
    }
}