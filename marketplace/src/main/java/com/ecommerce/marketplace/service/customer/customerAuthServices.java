package com.ecommerce.marketplace.service.customer;


import com.ecommerce.marketplace.Mapping.customerMapper;
import com.ecommerce.marketplace.Repository.customer.customerRepo;
import com.ecommerce.marketplace.Repository.customer.refreshTokenRepo;
import com.ecommerce.marketplace.dto.customer.customerRequestDTO;
import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.entities.customer.Securitytoken;
import com.ecommerce.marketplace.exceptions.userAlreadyExistsException;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class customerAuthServices implements UserDetailsService {

    private final customerRepo customerRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final refreshTokenRepo refreshTokenRepo;

    private customerMapper customerMapper = Mappers.getMapper(customerMapper.class);

    public customerAuthServices(customerRepo customerRepo, BCryptPasswordEncoder passwordEncoder,refreshTokenRepo refreshTokenRepo) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepo=refreshTokenRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepo.findByEmail(username);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found with email: " + username);
        }
        return customer;
    }


    public void registerUser(customerRequestDTO customerRequestDto) {

        System.out.println("before auth service");
        Optional.ofNullable(customerRepo.findByEmailOrPhone(
                customerRequestDto.getEmail(), customerRequestDto.getPhoneNumber())).ifPresent(user -> {
            throw new userAlreadyExistsException("User already exists with the provided email or phone number.");
        });

        customerRequestDto.setPassword(passwordEncoder.encode(customerRequestDto.getPassword()));

        System.out.println("after encoding password");
        System.out.println("before saving customer");
        Customer cus = customerMapper.dtoToEntity(customerRequestDto);
        System.out.println("username: " +cus.getUsername()+" email: "+cus.getEmail()+" phone: "+cus.getPhoneNumber());
        customerRepo.save(cus);
        System.out.println();
        System.out.println("after saving customer");
    }

    public Securitytoken saveSecurityToken(String token,String email) {

        Securitytoken securitytoken = new Securitytoken();
        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found with email: " + email);
        }


        else {
            securitytoken.setCustomer(customer);
            securitytoken.setHashToken(DigestUtils.sha256Hex(token));
            securitytoken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
            securitytoken.setRevoked(true);

            return refreshTokenRepo.save(securitytoken);

        }


    }
}