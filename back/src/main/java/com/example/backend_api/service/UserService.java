package com.example.backend_api.service;

import com.example.backend_api.model.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File("src/main/resources/users.json");
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAll() {
        try {
            return objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public User getById(String email) {
        return getAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public User save(User user) {
        List<User> users = getAll();

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        users.add(user);
        writeJson(users);
        return user;
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void writeJson(List<User> users) {
        try {
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}