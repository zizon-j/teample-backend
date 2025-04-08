package com.example.teample.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean exitsByEmail(String email);
}
