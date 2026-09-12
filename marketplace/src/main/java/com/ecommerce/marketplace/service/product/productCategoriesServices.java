package com.ecommerce.marketplace.service.product;


import com.ecommerce.marketplace.Mapping.productMapper;
import com.ecommerce.marketplace.Repository.product.productCategoriesRepo;
import com.ecommerce.marketplace.Repository.product.productRepo;
import com.ecommerce.marketplace.dto.product.categoryDTO;
import com.ecommerce.marketplace.dto.product.productDTO;
import com.ecommerce.marketplace.entities.product.products;
import com.ecommerce.marketplace.exceptions.IdNotFoundException;
import com.ecommerce.marketplace.exceptions.categoryNotFoundException;
import com.ecommerce.marketplace.projections.products.productResponse;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class productCategoriesServices {

    private final productCategoriesRepo productCategoriesRepo;
    private final productRepo productRepo;

    private productMapper productMapper = Mappers.getMapper(productMapper.class);

    public productCategoriesServices(productCategoriesRepo productCategoriesRepo, productRepo productRepo){
        this.productRepo=productRepo;
        this.productCategoriesRepo=productCategoriesRepo;
    }

    public void addCategory(categoryDTO request) {

        com.ecommerce.marketplace.entities.product.productCategories info = productCategoriesRepo.findByCategoryName(request.getParentCategoryName()).orElseThrow(
                ()-> new IdNotFoundException("Category Not Found")
        );

            com.ecommerce.marketplace.entities.product.productCategories productCategories= new com.ecommerce.marketplace.entities.product.productCategories();
            productCategories.setCategoryName(request.getCategoryName());
            productCategories.setParentCategoryId(info);
            productCategoriesRepo.save(productCategories);


    }

    public List<categoryDTO> getCategoriesAndTheirParentCategories() {

       List<categoryDTO> category = productCategoriesRepo.findCategoriesAndSubCategories();

       return  (category.isEmpty())?List.of(): category.stream().toList();
    }


    public List<String> getCategories() {

        List<String> category = productCategoriesRepo.findAllCategories();

         if (category==null || category.isEmpty()) {
             throw new categoryNotFoundException("Categories not found");
         }

         else {
             return category;
         }
    }

    @Transactional(rollbackOn = Exception.class)
    public Long deleteCategories(String name) {

        name = name.replace("-"," ").toLowerCase();

        Long rows = productCategoriesRepo.deleteByCategoryName(name);

        if (rows<1) {
            throw new categoryNotFoundException("Category Not Found");
        }

        return rows;
    }

}
