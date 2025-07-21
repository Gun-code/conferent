package com.conferent.persistence.repository;

import com.conferent.business.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryInterface {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
} 