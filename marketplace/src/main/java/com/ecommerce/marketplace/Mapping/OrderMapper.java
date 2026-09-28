package com.ecommerce.marketplace.Mapping;

import com.ecommerce.marketplace.dto.OrderSystem.OrderRequestDTO;
import com.ecommerce.marketplace.dto.cartSystem.cartRequestDTO;
import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.entities.orders.Orders;
import com.ecommerce.marketplace.entities.orders.ShippingAddress;
import com.ecommerce.marketplace.entities.orders.orderItems;
import com.ecommerce.marketplace.entities.orders.sellerOrderBoard;
import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.enums.OrderStatus;
import com.ecommerce.marketplace.enums.SellerOrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    cartRequestDTO itemToCart(OrderRequestDTO order);

    @Mapping(source = "orderDateTime",target = "createdAt")
    @Mapping(source = "address",target = "shippingAddress")
    Orders toEntity(Customer customer, BigDecimal totalAmount, OrderStatus status, ShippingAddress address, LocalDateTime orderDateTime);

    @Mapping(target = "orders", source = "orders")
    @Mapping(target = "seller", source = "seller")
    @Mapping(target = "status", source = "status")
    sellerOrderBoard toSellerBoardEntity(Orders orders, Seller seller, SellerOrderStatus status);

    @Mapping(target = "sellerOrder", source = "sellerOrder")
    @Mapping(source = "price", target = "unitPrice")
    @Mapping(target = "productListing", source = "productListings")
    orderItems toOrderItemsEntity(productListings productListings, sellerOrderBoard sellerOrder, Integer quantity, BigDecimal price);
}
