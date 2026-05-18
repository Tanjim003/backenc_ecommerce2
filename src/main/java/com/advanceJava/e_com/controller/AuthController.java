package com.advanceJava.e_com.controller;

import com.advanceJava.e_com.dto.UserDTO;
import com.advanceJava.e_com.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDto) {
        return ResponseEntity.ok(authService.register(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getRole()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(org.springframework.security.core.Authentication auth) {
        UserDTO safeUser = new UserDTO();
        safeUser.setUsername(auth.getName());
        safeUser.setRole(auth.getAuthorities().toString()); // or extract properly
        // Do NOT set password
        return ResponseEntity.ok(safeUser);
    }

}
