package com.ecommerce.marketplace.service.seller;

import com.ecommerce.marketplace.Mapping.sellerMapper;
import com.ecommerce.marketplace.Repository.sellerRepo;
import com.ecommerce.marketplace.dto.seller.sellerRequestDTO;
import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.exceptions.userAlreadyExistsException;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class sellerAuthServices {
    
    private final sellerRepo sellerRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    private sellerMapper sellerMapper = Mappers.getMapper(sellerMapper.class);
    
    public sellerAuthServices(sellerRepo sellerRepo,BCryptPasswordEncoder passwordEncoder) {
        this.sellerRepo=sellerRepo;
        this.passwordEncoder=passwordEncoder;
    }
    

    public String registerUser(sellerRequestDTO sellerRequestDTO) {

        System.out.println("before auth service");
        System.out.println(sellerRequestDTO.getEmail()+" "+sellerRequestDTO.getPhoneNumber());

        Optional.ofNullable(sellerRepo.findByEmailOrPhone(
                sellerRequestDTO.getEmail(), sellerRequestDTO.getPhoneNumber())).ifPresent(user -> {
            throw new userAlreadyExistsException("User already exists with the provided email or phone number.");
        });

        sellerRequestDTO.setPassword(passwordEncoder.encode(sellerRequestDTO.getPassword()));

        System.out.println("after encoding password");
        System.out.println("before saving customer");

        Seller s = sellerRepo.save(sellerMapper.toEntity(sellerRequestDTO));


        System.out.println(sellerRequestDTO.getEmail() +" "+ sellerRequestDTO.getPassword()+" "+sellerRequestDTO.getName()+" "+sellerRequestDTO.getStoreName()+" "+sellerRequestDTO.getStoreDescription()+" "+sellerRequestDTO.getPhoneNumber()+" "+sellerRequestDTO.getSellerAddress().getCity());

        return s.getUsername();
    }
}
