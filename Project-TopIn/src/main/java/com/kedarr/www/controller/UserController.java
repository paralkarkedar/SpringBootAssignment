package com.kedarr.www.controller;

import java.util.List;
import java.util.UUID;

import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kedarr.www.model.User;
import com.kedarr.www.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	UserService service;

	@PostMapping("/create_user")
	public User createUser(@RequestBody User user) {
		return service.createUser(user);
	}

	@PostMapping("/get_users")
	public List<User> getUsers(@RequestParam(required = false) UUID userId,
			@RequestParam(required = false) String mobNum, @RequestParam(required = false) UUID managerId) {
		return service.getUsers(userId, mobNum, managerId);
	}

	@PostMapping("/delete_user")
	public ResponseEntity<HttpStatus> deleteUser(@RequestParam UUID userId) throws RelationNotFoundException {
		return service.deleteUser(userId);

	}

	@PostMapping("/update_user")
	public ResponseEntity<User> updateUser(@RequestParam UUID userId, @RequestBody User user) {
		return service.updateUser(userId, user);
	}
}
