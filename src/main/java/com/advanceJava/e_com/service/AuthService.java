package com.advanceJava.e_com.service;

import com.advanceJava.e_com.dto.UserDTO;
import com.advanceJava.e_com.models.User;
import com.advanceJava.e_com.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO register(String username, String password, String role) {
        if (repo.findByUserName(username).isPresent())
            throw new IllegalStateException("Username already taken");

        User user = new User();          // ✅ variable named 'user'
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role == null ? "CUSTOMER" : role);
        User saved = repo.save(user);    // ✅ saved entity

        // Convert to DTO (without password)
        UserDTO dto = new UserDTO();
        dto.setId(saved.getId());
        dto.setUsername(saved.getUsername());
        dto.setRole(saved.getRole());
        // password is intentionally omitted
        return dto;
    }
}

