package com.ecommerce.marketplace.controller.Customer.Authentication;

import com.ecommerce.marketplace.dto.LoginRequestDTO;
import com.ecommerce.marketplace.dto.customer.customerRequestDTO;
import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.entities.customer.Securitytoken;
import com.ecommerce.marketplace.service.TokenBlockListService;
import com.ecommerce.marketplace.service.customer.customerAuthServices;
import com.ecommerce.marketplace.service.jwtService;
import com.ecommerce.marketplace.util.AuthUtil.AuthenticationHelper;
import com.ecommerce.marketplace.util.AuthUtil.JWTUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/customer-auth")
public class customerAuthController {

    private final customerAuthServices customerAuthService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationHelper authenticationHelper;
    private final jwtService jwtService;


    public customerAuthController(customerAuthServices customerAuthService, AuthenticationManager authenticationManager,
            AuthenticationHelper authenticationHelper,jwtService jwtService) {
        this.customerAuthService = customerAuthService;
        this.authenticationManager = authenticationManager;
        this.authenticationHelper = authenticationHelper;
        this.jwtService=jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody customerRequestDTO customerRequestDTO) {
        // Logic to register the customer
        System.out.println("before register");
        customerAuthService.registerUser(customerRequestDTO); // Replace with actual registration logic
        System.out.println("after register");
        return ResponseEntity.ok(customerRequestDTO.getName() + " registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {

        System.out.println("login request entering");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
            System.out.println("exiting");

            if (!authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("username or password is incorrect");
            } else {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                log.info("authentication principal,{}", ((UserDetails) authentication.getPrincipal()).getUsername());
                String[] tokens = authenticationHelper.generateOpaqueRefreshToken(loginRequest.getUsername());
                log.info("refresh Token,{}", tokens[0]);
                ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens[0])
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("Lax")
                        .path("api/auth/refresh")
                        .maxAge(Duration.ofMinutes(20))
                        .build();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, String.valueOf(cookie)).body(tokens[1]);

            }
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("username or password is incorrect");
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String rawToken) {

        if (rawToken == null)
            return ResponseEntity.status(401).build();
        String accessToken = authenticationHelper.isValid(rawToken);
        try {

            if (accessToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("login again");
            } else {

                return ResponseEntity.ok().body(accessToken);
            }
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("login again");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@CookieValue(name = "refreshToken", required = false) String rawToken, String accessToken) {

        if (rawToken==null && accessToken==null) {
            return ResponseEntity.status(401).build();
        }
        else {
            jwtService.BlackList(accessToken);
            Securitytoken revoked = authenticationHelper.revokeToken(rawToken);


            if (revoked.isRevoked()) {
                return ResponseEntity.ok().body("Log out is succcessful");
            }
            else {
                return ResponseEntity.status(400).body("bad request");
            }

        }
    }

}
