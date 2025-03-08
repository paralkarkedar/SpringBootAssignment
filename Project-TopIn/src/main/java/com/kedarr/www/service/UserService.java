package com.kedarr.www.service;

import java.util.List;
import java.util.UUID;

import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kedarr.www.exception.ResourceNotFoundException;
import com.kedarr.www.model.User;
import com.kedarr.www.repo.ManagerRepository;
import com.kedarr.www.repo.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	ManagerRepository managerRepository;

	public User createUser(User user) {
		if (user.getManagerId() != null && !managerRepository.existsByManagerIdAndIsActiveTrue(user.getManagerId())) {
			throw new IllegalArgumentException("Invalid manager ID");
		}
		
	    if (user.getMobNum() != null) {
	        user.setMobNum(user.getMobNum().replaceFirst("^(\\+91|0)", ""));
	    }

	    if (user.getPanNum() != null) {
	        user.setPanNum(user.getPanNum().toUpperCase());
	    }
		return userRepository.save(user);
	}

	public List<User> getUsers(UUID userId, String mobNum, UUID managerId) {
		if (userId != null)
			return List.of(userRepository.findById(userId).orElse(null));
		if (mobNum != null)
			return List.of(userRepository.findByMobNum(mobNum).orElse(null));
		if (managerId != null)
			return userRepository.findByManagerId(managerId);
		return userRepository.findAll();
	}

	public ResponseEntity<HttpStatus> deleteUser(UUID userId) throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found."));
		userRepository.delete(user);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<User> updateUser(UUID userId, User updatedUser) {

		if (userRepository.existsById(userId)) {
			User existingUser = userRepository.findById(userId).get();
			if (updatedUser.getFullName() != null)
				existingUser.setFullName(updatedUser.getFullName().replaceFirst("^(\\+91|0)", ""));
			if (updatedUser.getMobNum() != null)
				existingUser.setMobNum(updatedUser.getMobNum());
			if (updatedUser.getPanNum() != null)
				existingUser.setPanNum(updatedUser.getPanNum().toUpperCase());
			
			existingUser.setUpdatedAt(java.time.LocalDateTime.now());

			userRepository.save(existingUser);

			return new ResponseEntity<>(existingUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
}
