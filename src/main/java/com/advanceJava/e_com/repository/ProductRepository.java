package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save (Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    void updateStock(Long id, int newStock);

}
