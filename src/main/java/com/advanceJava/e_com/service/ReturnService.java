package com.advanceJava.e_com.service;

import com.advanceJava.e_com.models.OrderItem;
import com.advanceJava.e_com.models.ReturnRequest;
import com.advanceJava.e_com.repository.ReturnRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnService {
    private final ReturnRepository repo;
    private final OrderService orderService;
    private final ProductService productService;

    public ReturnService(ReturnRepository repo, OrderService orderService, ProductService productService) {
        this.repo = repo; this.orderService = orderService; this.productService = productService;
    }

    public ReturnRequest requestReturn(Long orderId, String reason) {
        var order = orderService.getById(orderId);
        if (!order.getStatus().equals("DELIVERED"))
            throw new IllegalStateException("Only DELIVERED orders can be returned");
        ReturnRequest r = new ReturnRequest();
        r.setOrderId(orderId);
        r.setReason(reason);
        r.setStatus("REQUESTED");
        return repo.save(r);
    }

    public List<ReturnRequest> getAll() {
        return repo.findAll();

    }

    public ReturnRequest getById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new RuntimeException("Return not found: " + id));
    }

    public void approve(Long returnId) {
        ReturnRequest r = getById(returnId);
        List<OrderItem> items = orderService.getItems(r.getOrderId());
        items.forEach(i -> productService.restoreStock(i.getProductId(), i.getQuantity()));
        repo.updateStatus(returnId, "APPROVED");
        orderService.updateStatus(r.getOrderId(), "RETURNED");
    }

    public void reject(Long returnId) {
        getById(returnId);
        repo.updateStatus(returnId, "REJECTED");
    }


}
