package com.ecommerce.marketplace.controller.Customer.Authentication;

import com.ecommerce.marketplace.dto.customer.customerAddressDTO;
import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.service.customer.customerServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer-addresses")
public class Addresses {

    private final customerServices customerServices;

    public Addresses(customerServices customerServices) {
        this.customerServices=customerServices;
    }

    @PostMapping("/address")
    public ResponseEntity<?> addAddress(@Valid @RequestBody customerAddressDTO customerAddress) {

        Address added = customerServices.addAddress(customerAddress);

        if (added==null) {
            return ResponseEntity.status(500).body("unable to add address");
        }
        else {
           return ResponseEntity.ok(added.getCustomer().getUsername()+" address is added");
        }
    }
}
