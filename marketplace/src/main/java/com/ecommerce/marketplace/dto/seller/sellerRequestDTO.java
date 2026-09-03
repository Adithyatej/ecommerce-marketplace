package com.ecommerce.marketplace.dto.seller;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class sellerRequestDTO {


    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "email is mandatory")
    @Email(message = "Email should be valid",regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;


    @NotBlank(message = "Password is mandatory")
    private String password;


    @NotBlank(message = "phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;


    @NotBlank(message = "store name is mandatory")
    private String storeName;

    @NotBlank(message = "store description is mandatory")
    private String storeDescription;



    private sellerAddressDTO sellerAddress;





}
