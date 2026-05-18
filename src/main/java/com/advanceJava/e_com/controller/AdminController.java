package com.advanceJava.e_com.controller;

import com.advanceJava.e_com.models.Product;
import com.advanceJava.e_com.service.OrderService;
import com.advanceJava.e_com.service.ProductService;
import com.advanceJava.e_com.service.ReturnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    private final ReturnService returnService;

    public AdminController(ProductService productService, OrderService orderService, ReturnService returnService) {
        this.productService = productService;
        this.orderService = orderService;
        this.returnService = returnService;
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam int stockQuantity,
            @RequestParam(required = false) MultipartFile image) throws Exception {

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            String uploadDir = "uploads/";
            new File(uploadDir).mkdirs();
            imagePath = uploadDir + UUID.randomUUID() + "_" + image.getOriginalFilename();
            image.transferTo(new File(imagePath));
        }

        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStockQuantity(stockQuantity);
        p.setImagePath(imagePath);
        return ResponseEntity.ok(productService.add(p));
    }

    @GetMapping("/products")
    public ResponseEntity<?> allProducts() { return ResponseEntity.ok(productService.getAll()); }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted"));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> allOrders() { return ResponseEntity.ok(orderService.getAllOrders()); }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> orderDetail(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("order", orderService.getById(id), "items", orderService.getItems(id)));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        orderService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok(Map.of("message", "Status updated to " + body.get("status")));
    }

    @GetMapping("/returns")
    public ResponseEntity<?> allReturns() { return ResponseEntity.ok(returnService.getAll()); }

    @PutMapping("/returns/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        returnService.approve(id);
        return ResponseEntity.ok(Map.of("message", "Return approved, stock restored"));
    }

    @PutMapping("/returns/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        returnService.reject(id);
        return ResponseEntity.ok(Map.of("message", "Return rejected"));
    }
}
