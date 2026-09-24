package com.ecommerce.marketplace.util.AuthUtil;

import com.ecommerce.marketplace.Repository.customer.refreshTokenRepo;
import com.ecommerce.marketplace.entities.customer.Securitytoken;
import com.ecommerce.marketplace.service.customer.customerAuthServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
@Slf4j
public class AuthenticationHelper {
    private final customerAuthServices customerAuthServices;
    private final JWTUtil jwtUtil;
    private final refreshTokenRepo refreshTokenRepo;

    public AuthenticationHelper(customerAuthServices customerAuthServices,JWTUtil jwtUtil,refreshTokenRepo refreshTokenRepo) {
        this.customerAuthServices= customerAuthServices;
        this.jwtUtil=jwtUtil;
        this.refreshTokenRepo=refreshTokenRepo;
    }

    public String[] generateOpaqueRefreshToken(String email) {

        String[] tokens= new String[2];
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        log.info("the random secure bytes are {}",bytes);
        String refreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        log.info("the refresh token is {}",refreshToken);

         Securitytoken token = customerAuthServices. saveSecurityToken(refreshToken,email);

         if (refreshToken!=null && !refreshToken.isEmpty()) {
                String accessToken = generateAccessToken(email);
                tokens[0] = refreshToken;
                tokens[1] = accessToken;
                log.info("access token is {}",accessToken);

         }
            return tokens;

    }

    public String isValid(String token) {

        String accessToken="";
       Securitytoken securitytoken = refreshTokenRepo.findByToken(DigestUtils.sha256Hex(token));

       if (securitytoken==null) {
           return "";
       }
       else {
           if (!LocalDateTime.now().isBefore(securitytoken.getExpiresAt())) {
               return "";
           }
           else  {
               if (securitytoken.isRevoked()) {
                   return "";
               }
               else {
                   return generateAccessToken(securitytoken.getCustomer().getEmail());

               }
           }
       }
    }



    public String generateAccessToken(String email) {
        return jwtUtil.generateToken(email);
    }


}
