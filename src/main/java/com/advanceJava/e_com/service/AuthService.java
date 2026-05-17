package com.advanceJava.e_com.service;

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

    public User register(String username, String password, String role) {
        if (repo.findByUserName(username).isPresent())
            throw new IllegalStateException("Username already taken");
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole(role == null ? "CUSTOMER" : role);
        return repo.save(u);
    }
}

