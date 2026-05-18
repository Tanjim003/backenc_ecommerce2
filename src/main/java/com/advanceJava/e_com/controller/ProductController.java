package com.advanceJava.e_com.controller;

import com.advanceJava.e_com.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(productService.getAll()); // List<ProductDTO>
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getByIdDTO(id)); // ProductDTO
    }

}
