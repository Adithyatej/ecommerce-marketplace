package com.ecommerce.marketplace.service.customer;

import com.ecommerce.marketplace.Mapping.customerMapper;
import com.ecommerce.marketplace.Repository.customer.customerAddressRepo;
import com.ecommerce.marketplace.Repository.customer.customerRepo;
import com.ecommerce.marketplace.dto.customer.customerAddressDTO;
import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.customer.Customer;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
public class customerServices {

    private final customerRepo customerRepo;
    private final customerAddressRepo customerAddressRepo;
    private final customerMapper customerMapper = Mappers.getMapper(customerMapper.class);

    public customerServices(customerRepo customerRepo,customerAddressRepo customerAddressRepo) {
        this.customerRepo=customerRepo;
        this.customerAddressRepo=customerAddressRepo;
    }

    @Transactional
    public Address addAddress(customerAddressDTO customerAddress) {

        Customer customer = customerRepo.findByEmail(customerAddress.getEmail());

        return customerAddressRepo.save(customerMapper.DtoToAddressEntity(customerAddress,customer));

    }
}
