package com.ecommerce.marketplace.service.order;

import com.ecommerce.marketplace.Mapping.AddressMapper;
import com.ecommerce.marketplace.Mapping.OrderMapper;
import com.ecommerce.marketplace.Repository.customer.customerRepo;
import com.ecommerce.marketplace.Repository.order.OrderItemsRepo;
import com.ecommerce.marketplace.Repository.order.OrderRepo;
import com.ecommerce.marketplace.Repository.order.sellerBoardRepo;
import com.ecommerce.marketplace.Repository.seller.sellerRepo;
import com.ecommerce.marketplace.dto.OrderSystem.OrderRequestDTO;
import com.ecommerce.marketplace.dto.cartSystem.cartRequestDTO;
import com.ecommerce.marketplace.entities.cart.cartItems;
import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.entities.orders.Orders;
import com.ecommerce.marketplace.entities.orders.ShippingAddress;
import com.ecommerce.marketplace.entities.orders.orderItems;
import com.ecommerce.marketplace.entities.orders.sellerOrderBoard;
import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.enums.OrderStatus;
import com.ecommerce.marketplace.enums.ProductListingStatus;
import com.ecommerce.marketplace.enums.SellerOrderStatus;
import com.ecommerce.marketplace.exceptions.AddressNotFoundException;
import com.ecommerce.marketplace.exceptions.ProductOutOfStockException;
import com.ecommerce.marketplace.service.cart.cartServices;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServices {

    private final OrderRepo orderRepo;
    private final OrderItemsRepo orderItemsRepo;
    private final cartServices cartServices;
    private final customerRepo customerRepo;
    private final sellerRepo sellerRepo;
    private final sellerBoardRepo sellerBoardRepo;

    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    public OrderServices(OrderRepo orderRepo, OrderItemsRepo orderItemsRepo,cartServices cartServices,customerRepo customerRepo,sellerRepo sellerRepo,sellerBoardRepo sellerBoardRepo) {
        this.orderRepo=orderRepo;
        this.orderItemsRepo=orderItemsRepo;
        this.cartServices=cartServices;
        this.customerRepo=customerRepo;
        this.sellerRepo=sellerRepo;
        this.sellerBoardRepo=sellerBoardRepo;

    }

    @Transactional
    public Boolean makeOrder(OrderRequestDTO cart) {

        List<cartItems> items=new ArrayList<>();
        List<Long> listings= new ArrayList<>();
        listings.add(cart.getListingId());
    // checking if buying exists
        System.out.println("before executing the order-items");
        items = orderItemsRepo.findOrderItem(cart.getEmail(),cart.getListingId());
        log.info("order items found: {}", items == null ? 0 : items.size());
        System.out.println("address query before");
        Address address = customerRepo.findAddressByEmail(cart.getEmail(), cart.getShippingAddress()).orElseThrow(()-> new AddressNotFoundException("Address does not exist. create a new one"));
        System.out.println("address query after execution"+ address.getArea()+" "+address.getStreet());
        if (items == null || items.isEmpty()) {
            cartRequestDTO cartItem = orderMapper.itemToCart(cart);
            items.add(cartServices.addProductToCart(cartItem));
        } else {
            for (cartItems cartItem : items) {
                if (cartItem.getProductListings().getStatus()== ProductListingStatus.CURRENTLY_OUT_OF_STOCK && cartItem.getProductListings().getStockQuantity()<cart.getQuantity()) {
                    System.out.println("if executed");
                    throw new ProductOutOfStockException("product seems to be out of stock");
                }
                else {
                    System.out.println("else executed");
                    cartItem.setQuantity(cart.getQuantity());
                }
            }
        }

       Boolean added =  createOrder(items, cart.getEmail(), address, listings);
        return added;
    }


    public Boolean createOrder(List<cartItems> cart,String email,Address address,List<Long> listings){

        System.out.println("create an order before execution");
        Orders order=null;
        sellerOrderBoard sellerOrder=null;
        orderItems orderItem=null;
        Customer customer = customerRepo.findByEmail(email);
        log.info("customer info is {},{}",customer.getEmail(),customer.getPhoneNumber());
        BigDecimal totalAmount = cart.stream().map(item -> item.getProductListings().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).toList().stream().reduce(BigDecimal.ZERO,BigDecimal::add);
        log.info("price is {}",totalAmount);
        LocalDateTime orderDateTime = LocalDateTime.now();
        log.info("local date and time is {}",orderDateTime);
        OrderStatus status= OrderStatus.CREATED;
        log.info("status is,{}",status);
        ShippingAddress shippingAddress = addressMapper.toShippingAddress(address);
        log.info("adress is {},{},{}",shippingAddress.getStreet(),shippingAddress.getArea(),shippingAddress.getCity());

        Orders orders = orderMapper.toEntity(customer,totalAmount,status,shippingAddress,orderDateTime);
        log.info("data is {},{},{}",orders.getCreatedAt(),orders.getId(),orders.getStatus());
        order = orderRepo.save(orders);
        System.out.println("order info is :"+ order.getId()+" "+order.getCustomer().getEmail()+" "+ order.getCreatedAt()+" "+order.getStatus());
        List<Seller> sellers = findAllSellerOfProduct(listings);
        System.out.println("sellers size is "+sellers.size());
        Map<Long,sellerOrderBoard> sellerOrderMap = new HashMap<>();
        for(Seller seller: sellers) {
           sellerOrder = sellerBoardRepo.save(orderMapper.toSellerBoardEntity(order,seller, SellerOrderStatus.PENDING));
           order.getSellerOrders().add(sellerOrder); //birectional mapping
            sellerOrderMap.put(seller.getId(),sellerOrder);
           log.info("items are {},{},{}",sellerOrder.getSeller().getUsername(),sellerOrder.getId(),sellerOrder.getOrders().getId());
        }

        for (int i=0;i<cart.size();i++) {
            productListings pl = cart.get(i).getProductListings();
            Seller sellerOfListing = pl.getSeller();
            sellerOrderBoard sellerBoard = sellerOrderMap.get(sellerOfListing.getId());
            orderItem = orderMapper.toOrderItemsEntity(pl, sellerBoard, cart.get(i).getQuantity(), pl.getPrice());
            orderItemsRepo.save(orderItem);
        }

        return orderItem!=null&&sellerOrder!=null&&order!=null;
    }

    public List<Seller> findAllSellerOfProduct(List<Long> listings) {

        List<Seller> sellers = sellerRepo.findSellersByListing(listings);
        return sellers;
    }

}
