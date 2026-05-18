package com.advanceJava.e_com.service;

import com.advanceJava.e_com.dto.OrderDTO;
import com.advanceJava.e_com.dto.OrderItemDTO;
import com.advanceJava.e_com.dto.ReturnRequestDTO;
import com.advanceJava.e_com.models.Order;
import com.advanceJava.e_com.models.OrderItem;
import com.advanceJava.e_com.models.ReturnRequest;
import com.advanceJava.e_com.repository.ReturnRepository;
import com.advanceJava.e_com.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReturnService {
    private final ReturnRepository repo;
    private final OrderService orderService;
    private final ProductService productService;

    public ReturnService(ReturnRepository repo, OrderService orderService, ProductService productService) {
        this.repo = repo;
        this.orderService = orderService;
        this.productService = productService;
    }

    public ReturnRequestDTO requestReturn(Long orderId, String reason) {
        OrderDTO order = orderService.getById(orderId); // returns OrderDTO
        if (!order.getStatus().equals("DELIVERED")) {
            throw new IllegalStateException("Only DELIVERED orders can be returned");
        }
        ReturnRequest r = new ReturnRequest();
        r.setOrderId(orderId);
        r.setReason(reason);
        r.setStatus("REQUESTED");
        ReturnRequest saved = repo.save(r);
        return DTOMapper.toReturnRequestDTO(saved);
    }

    public List<ReturnRequestDTO> getAll() {
        List<ReturnRequest> requests = repo.findAll();
        return requests.stream()
                .map(DTOMapper::toReturnRequestDTO)
                .collect(Collectors.toList());
    }

    public ReturnRequestDTO getById(Long id) {
        ReturnRequest r = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Return not found: " + id));
        return DTOMapper.toReturnRequestDTO(r);
    }

    public ReturnRequestDTO approve(Long returnId) {
        ReturnRequest r = getByIdEntity(returnId);
        // Use the new method to get OrderItem entities
        List<OrderItem> items = orderService.getOrderItemsEntities(r.getOrderId());
        items.forEach(i -> productService.restoreStock(i.getProductId(), i.getQuantity()));
        repo.updateStatus(returnId, "APPROVED");
        orderService.updateStatus(r.getOrderId(), "RETURNED");
        return getById(returnId);
    }

    public void reject(Long returnId) {
        // No need to fetch, just update
        repo.updateStatus(returnId, "REJECTED");
    }

    // Helper to get the entity (for internal use only)
    private ReturnRequest getByIdEntity(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Return not found: " + id));
    }

    public Object getReturnsForUser(Long userId) {
        // 1. Get all order IDs belonging to this user
        List<Order> userOrders = orderService.getMyOrdersEntities(userId);
        Set<Long> orderIds = userOrders.stream()
                .map(Order::getId)
                .collect(Collectors.toSet());

        // 2. Fetch all return requests from the repository
        List<ReturnRequest> allReturns = repo.findAll();

        // 3. Filter and convert to DTOs
        return allReturns.stream()
                .filter(r -> orderIds.contains(r.getOrderId()))
                .map(DTOMapper::toReturnRequestDTO)
                .collect(Collectors.toList());
    }
}