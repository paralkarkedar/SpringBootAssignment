package com.kedarr.www.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kedarr.www.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByMobNum(String mobNum);

	List<User> findByManagerId(UUID managerId);
}
