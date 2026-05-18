package com.advanceJava.e_com.service;

import com.advanceJava.e_com.dto.OrderDTO;
import com.advanceJava.e_com.dto.OrderItemDTO;
import com.advanceJava.e_com.models.CartItem;
import com.advanceJava.e_com.models.Order;
import com.advanceJava.e_com.models.OrderItem;
import com.advanceJava.e_com.models.Product;
import com.advanceJava.e_com.repository.CartRepository;
import com.advanceJava.e_com.repository.OrderRepository;
import com.advanceJava.e_com.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepo, CartRepository cartRepo, ProductService productService){
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.productService = productService;

    }

    public Order checkout(Long userId, String street, String city, String postalCode) {
        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            Product p = productService.getById(ci.getProductId());
            if (p.getStockQuantity() < ci.getQuantity())
                throw new IllegalStateException("Insufficient stock: " + p.getName());
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("CONFIRMED");
        order.setTotalAmount(total);
        order.setStreet(street);
        order.setCity(city);
        order.setPostalCode(postalCode);
        Order saved = orderRepo.save(order);

        for (CartItem ci : cartItems) {
            Product p = productService.getById(ci.getProductId());
            OrderItem oi = new OrderItem();
            oi.setOrderId(saved.getId());
            oi.setProductId(ci.getProductId());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtPurchase(p.getPrice());
            orderRepo.saveOrderItem(oi);
            productService.reduceStock(ci.getProductId(), ci.getQuantity());


        }

        cartRepo.clearByUserId(userId);
        return saved;
    }

    public List<OrderDTO> getMyOrders(Long userId) {
        List<Order> orders = orderRepo.findByUserId(userId);
        return orders.stream()
                .map(DTOMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public List<Order> getMyOrdersEntities(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public OrderDTO getById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        return DTOMapper.toOrderDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream()
                .map(DTOMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getOrderWithItems(Long orderId) {
        OrderDTO order = getById(orderId);
        List<OrderItem> items = orderRepo.findItemsByOrderId(orderId);
        List<OrderItemDTO> itemDTOs = items.stream()
                .map(item -> {
                    Product product = productService.getById(item.getProductId());
                    return DTOMapper.toOrderItemDTO(item, product.getName());
                })
                .collect(Collectors.toList());
        return Map.of("order", order, "items", itemDTOs);
    }

//    public List<OrderItem> getItems(Long orderId) {
//        return orderRepo.findItemsByOrderId(orderId);
//    }

    public void updateStatus(Long orderId, String status) {
        orderRepo.updateStatus(orderId, status);
    }


    public List<OrderItemDTO> getItems(Long orderId) {
        // 1. Fetch entity list from repository
        List<OrderItem> items = orderRepo.findItemsByOrderId(orderId);

        // 2. Convert each to OrderItemDTO including product name
        return items.stream()
                .map(item -> {
                    Product product = productService.getById(item.getProductId());
                    return DTOMapper.toOrderItemDTO(item, product.getName());
                })
                .collect(Collectors.toList());
    }
    // OrderService.java
    public List<OrderItem> getOrderItemsEntities(Long orderId) {
        return orderRepo.findItemsByOrderId(orderId);
    }


}
