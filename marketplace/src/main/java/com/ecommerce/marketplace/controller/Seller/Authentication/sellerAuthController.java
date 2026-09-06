package com.ecommerce.marketplace.controller.Seller.Authentication;

import com.ecommerce.marketplace.dto.seller.sellerRequestDTO;
import com.ecommerce.marketplace.entities.seller.seller;
import com.ecommerce.marketplace.service.seller.sellerAuthServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seller-auth")
public class sellerAuthController {

    private final sellerAuthServices sellerAuthService;
    public sellerAuthController(sellerAuthServices sellerAuthService) {
        this.sellerAuthService=sellerAuthService;
    }

        @PostMapping("/register")

        public ResponseEntity<?> registerSeller(@Valid @RequestBody sellerRequestDTO sellerRequestDTO) {

            System.out.println("before register");
            String registeredUser = sellerAuthService.registerUser(sellerRequestDTO);
            System.out.println("after register");
            return ResponseEntity.ok(registeredUser + " registered successfully");
        }


    }


