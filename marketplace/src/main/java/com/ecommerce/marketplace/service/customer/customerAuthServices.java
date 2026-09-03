package com.ecommerce.marketplace.service.customer;


import com.ecommerce.marketplace.Mapping.customerMapper;
import com.ecommerce.marketplace.Repository.customerRepo;
import com.ecommerce.marketplace.dto.customer.customerRequestDTO;
import com.ecommerce.marketplace.entities.customer.customer;
import com.ecommerce.marketplace.exceptions.userAlreadyExistsException;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class customerAuthServices implements UserDetailsService {

    private final customerRepo customerRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    private customerMapper customerMapper = Mappers.getMapper(customerMapper.class);

    public customerAuthServices(customerRepo customerRepo, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
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
        customer cus = customerMapper.dtoToEntity(customerRequestDto);
        System.out.println("username: " +cus.getUsername()+" email: "+cus.getEmail()+" phone: "+cus.getPhoneNumber());
        customerRepo.save(cus);
        System.out.println();
        System.out.println("after saving customer");
    }
}