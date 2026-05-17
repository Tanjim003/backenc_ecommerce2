package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.ReturnRequest;

import java.util.List;
import java.util.Optional;

public interface ReturnRepository {
    ReturnRequest save(ReturnRequest r);
    Optional<ReturnRequest> findById(Long id);
    List<ReturnRequest> findAll();
    List<ReturnRequest> findByOrderId(Long orderId);
    void updateStatus(Long id, String status);
}
