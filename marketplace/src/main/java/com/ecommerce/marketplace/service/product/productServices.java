package com.ecommerce.marketplace.service.product;

import com.ecommerce.marketplace.Mapping.productMapper;
import com.ecommerce.marketplace.Repository.product.productCategoriesRepo;
import com.ecommerce.marketplace.Repository.product.productRepo;
import com.ecommerce.marketplace.dto.product.categoryDTO;
import com.ecommerce.marketplace.dto.product.productDTO;
import com.ecommerce.marketplace.entities.product.productCategories;
import com.ecommerce.marketplace.entities.product.products;
import com.ecommerce.marketplace.exceptions.IdNotFoundException;
import com.ecommerce.marketplace.exceptions.categoryNotFoundException;
import com.ecommerce.marketplace.projections.products.productResponse;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class productServices {


        private final productCategoriesRepo productCategoriesRepo;
        private final productRepo productRepo;

        private productMapper productMapper = Mappers.getMapper(productMapper.class);

        public productServices(productCategoriesRepo productCategoriesRepo, productRepo productRepo){
            this.productRepo=productRepo;
            this.productCategoriesRepo=productCategoriesRepo;
        }



        public String addProducts(productDTO product) {

            productCategories info = productCategoriesRepo.findByCategoryName(product.getProductCategory()).orElseThrow(
                    ()-> new IdNotFoundException("Category Not Found")
            );

            products productContent = productRepo.save(productMapper.toEntity(product,info));

            return productContent.getProductName();

        }

        public List<productResponse> getProducts() {

            List<productResponse> products = productRepo.findProductsWithCategory();
            if (products==null || products.isEmpty()) {
                throw new categoryNotFoundException("Categories not found");
            }

            else {
                return products;
            }
        }

        public productDTO getProductsByName(String name) {

            name = name.replace("-"," ").toLowerCase();

            products product = productRepo.findByProductName(name);
            if (product==null) {
                throw new IdNotFoundException("Product Not Found");
            }

            else {
                return productMapper.toDTO(product);
            }

        }


        @Transactional
    public @Nullable String updateProduct(String name, productDTO product) {

            name = name.replace("-"," ").toLowerCase();

            products existingProduct = productRepo.findByProductName(name);

            if (existingProduct==null) {
                throw new IdNotFoundException("Product Not Found");
            }
            else {
                if (product.getProductName() != null) {
                    existingProduct.setProductName(product.getProductName());
                }
                if (product.getProductDescription() != null) {
                    existingProduct.setProductDescription(product.getProductDescription());
                }
                if (product.getBrand() != null) {
                    existingProduct.setBrand(product.getBrand());
                }
                if (product.getProductCategory() != null) {
                    productCategories productCategory = productCategoriesRepo.findByCategoryName(product.getProductName()).orElseThrow(() -> new IdNotFoundException("Category Not Found"));
                    existingProduct.setProductCategories(productCategory);
                }

                products updatedProduct = productRepo.save(existingProduct);
                return updatedProduct.getProductName();
            }
        }


    @Transactional
    public Object deleteProduct(String name) {
            name = name.replace("-"," ").toLowerCase();

            products product = productRepo.deleteByProductName(name).orElseThrow( ()-> new IdNotFoundException("Product Not Found"));

            return product.getProductName();
    }
}
