package com.advanceJava.e_com.service;

import com.advanceJava.e_com.dto.ProductDTO;
import com.advanceJava.e_com.models.Product;
import com.advanceJava.e_com.repository.ProductRepository;
import com.advanceJava.e_com.util.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public ProductDTO add(Product p) {
        Product saved = repo.save(p);
        return DTOMapper.toProductDTO(saved);
    }

    public List<ProductDTO> getAll() {
        List<Product> products = repo.findAll();
        return products.stream()
                .map(DTOMapper::toProductDTO)
                .collect(Collectors.toList());
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

    public Object getByIdDTO(Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return DTOMapper.toProductDTO(product);
    }
}
