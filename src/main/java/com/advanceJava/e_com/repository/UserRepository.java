package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUserName(String username);


}
