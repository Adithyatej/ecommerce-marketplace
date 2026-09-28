package com.ecommerce.marketplace.Mapping;


import com.ecommerce.marketplace.dto.product.productDTO;
import com.ecommerce.marketplace.dto.product.productListingDTO;
import com.ecommerce.marketplace.entities.product.productCategories;
import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.entities.product.products;
import com.ecommerce.marketplace.entities.seller.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface productMapper {


    @Mapping(target = "productCategories",source = "category")
    products toEntity(productDTO product, productCategories category);

    @Mapping(target = "productCategory",source = "productCategories.categoryName")
    productDTO toDTO(products product);


    @Mapping(source = "product",target = "product")
    @Mapping(source = "seller",target = "seller")
    productListings toProductListEntity(productListingDTO productList, products product, Seller seller);
}
