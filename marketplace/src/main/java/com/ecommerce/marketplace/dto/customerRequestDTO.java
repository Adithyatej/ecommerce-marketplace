package com.ecommerce.marketplace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

public class customerRequestDTO {

    @Setter
    @Getter
    @NotBlank(message = "Name is mandatory")
    private String name;
    @Setter
    @Getter
    @Email(message = "Email should be valid",regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;
    @Setter
    @Getter
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Setter
    @Getter
    @NotBlank(message = "phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;



    public customerRequestDTO() {
    }

    public customerRequestDTO(String name, String email, String password, String address, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

}
