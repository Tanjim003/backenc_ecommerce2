package com.advanceJava.e_com.repository;


import com.advanceJava.e_com.models.Order;
import com.advanceJava.e_com.models.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByUserId(Long userId);
    void updateStatus(Long id, String status);
    void saveOrderItem(OrderItem item);
    List<OrderItem> findItemsByOrderId(Long orderId);


}
