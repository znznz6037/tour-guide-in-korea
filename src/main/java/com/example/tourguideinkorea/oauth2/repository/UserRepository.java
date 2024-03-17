package com.example.tourguideinkorea.oauth2.repository;

import com.example.tourguideinkorea.oauth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);
}
