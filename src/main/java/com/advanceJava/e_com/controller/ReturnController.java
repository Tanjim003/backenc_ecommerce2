package com.advanceJava.e_com.controller;

import com.advanceJava.e_com.repository.UserRepository;
import com.advanceJava.e_com.service.ReturnService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/returns")
public class ReturnController {

    private final ReturnService returnService;
    private final UserRepository userRepository;

    public ReturnController(ReturnService returnService, UserRepository userRepository) {
        this.returnService = returnService;
        this.userRepository = userRepository;
    }


    private Long userId(Authentication auth) {
        String username = auth.getName();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username))
                .getId();
    }

    @PostMapping("/request")
    public ResponseEntity<?> request(@Valid @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(returnService.requestReturn(
                Long.valueOf(body.get("orderId")), body.get("reason")));
    }

    @GetMapping("/my")
    public ResponseEntity<?> myReturns(Authentication auth) {
        return ResponseEntity.ok(returnService.getReturnsForUser(userId(auth)));
    }


    @GetMapping  // for admin
    public ResponseEntity<?> allReturns() {
        return ResponseEntity.ok(returnService.getAll()); // List<ReturnRequestDTO>
    }
}
