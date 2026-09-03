package com.ecommerce.marketplace.controller.Customer.Authentication;


import com.ecommerce.marketplace.dto.customer.customerRequestDTO;
import com.ecommerce.marketplace.service.customer.customerAuthServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer-auth")
public class customerAuthController {

    private final customerAuthServices customerAuthService;

    public customerAuthController(customerAuthServices customerAuthService) {
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
