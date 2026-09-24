package com.ecommerce.marketplace.service;


import com.ecommerce.marketplace.util.AuthUtil.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class jwtService {

    private final UserDetailsService userDetailsService;

    private final JWTUtil jwtUtil;

    public jwtService(UserDetailsService userDetailsService,JWTUtil jwtUtil) {
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
    }


        private Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .verifyWith(jwtUtil.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {

        String username=  extractClaim(token, Claims::getSubject);
        return username;

    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }


    public boolean isTokenValid(String token) {
        if (extractUsername(token)!=null && !isTokenExpired(token)) {
            return true;
        }
        else {
            return false;
        }
    }
}
