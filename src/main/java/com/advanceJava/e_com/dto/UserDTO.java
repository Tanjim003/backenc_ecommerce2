package com.advanceJava.e_com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be 6-100 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;

    public UserDTO() {}

    public UserDTO(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // ... all getters/setters unchanged
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}