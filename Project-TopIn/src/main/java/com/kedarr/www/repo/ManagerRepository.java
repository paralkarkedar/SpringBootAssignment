package com.kedarr.www.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kedarr.www.model.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
	boolean existsByManagerIdAndIsActiveTrue(UUID managerId);
}
