package com.example.usermanagement.repository;

import com.example.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByMobNumAndIsActiveTrue(String mobNum);
    List<User> findByManagerIdAndIsActiveTrue(UUID managerId);
    List<User> findAllByIsActiveTrue();
}