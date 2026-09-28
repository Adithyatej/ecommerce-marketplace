package com.ecommerce.marketplace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {

    @NotBlank(message = "username is missing")
    @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String username;

    @NotBlank(message = "password is missing")
    private String password;
}
