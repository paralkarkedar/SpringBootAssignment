package com.example.usermanagement.repository;

import com.example.usermanagement.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    Optional<Manager> findByManagerIdAndIsActiveTrue(UUID managerId);
}