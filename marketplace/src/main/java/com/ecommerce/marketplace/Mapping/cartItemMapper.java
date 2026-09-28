package com.ecommerce.marketplace.Mapping;

import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.entities.cart.customerCart;
import com.ecommerce.marketplace.entities.product.productListings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface cartItemMapper {

     @Mapping(target = "productListings", source = "productList")
     cartItems toEntity(customerCart cart, productListings productList,Integer quantity);
}
