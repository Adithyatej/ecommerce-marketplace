package com.ecommerce.marketplace.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/category-api/category/**").permitAll()
                        .requestMatchers("/category-api/category", "/category-api/categories/**").permitAll()
                        .requestMatchers("/products-api/**").permitAll()
                        .requestMatchers("/api/seller-auth/**").permitAll()
                        .requestMatchers("/api/customer-auth/**").permitAll()
                        .requestMatchers("/api/productsList/**").permitAll()
                        .requestMatchers("/api/cart/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}