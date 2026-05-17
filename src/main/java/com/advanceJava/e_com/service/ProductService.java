package com.advanceJava.e_com.service;

import com.advanceJava.e_com.models.Product;
import com.advanceJava.e_com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product add(Product p) {
        return repo.save(p);
    }

    public List<Product> getAll() {
        return repo.findAll();

    }
    public Product getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void reduceStock(Long id, int qty) {
        Product p = getById(id);
        if (p.getStockQuantity() < qty)
            throw new IllegalStateException("Insufficient stock: " + p.getName());
        repo.updateStock(id, p.getStockQuantity() - qty);
    }

    public void restoreStock(Long id, int qty) {
        Product p = getById(id);
        repo.updateStock(id, p.getStockQuantity() + qty);
    }

}
