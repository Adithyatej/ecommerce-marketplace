package com.ecommerce.marketplace.controller.Customer.Authentication;


import com.ecommerce.marketplace.dto.customerRequestDTO;
import com.ecommerce.marketplace.service.customer.authservices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer-auth")
public class customerAuthController {

    private final authservices customerAuthService;

    public customerAuthController(authservices customerAuthService) {
        this.customerAuthService = customerAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody customerRequestDTO customerRequestDTO) {
        // Logic to register the customer
        System.out.println("before register");
        customerAuthService.registerUser(customerRequestDTO); // Replace with actual registration logic
        System.out.println("after register");
        return ResponseEntity.ok(customerRequestDTO.getName() + " registered successfully");
    }



}
