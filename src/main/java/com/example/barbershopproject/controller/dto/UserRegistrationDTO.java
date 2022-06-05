package com.example.barbershopproject.controller.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String login;
    private String password;
}
