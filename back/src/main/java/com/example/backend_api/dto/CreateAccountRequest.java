package com.example.backend_api.dto;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String username;
    private String firstname;
    private String email;
    private String password;
}