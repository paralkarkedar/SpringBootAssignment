package com.example.usermanagement.controller;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/create_user")
    public ResponseEntity<ApiResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        log.info("Received request to create user: {}", request);
        ApiResponseDto response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get_users")
    public ResponseEntity<ApiResponseDto> getUsers(@RequestBody(required = false) GetUsersRequestDto request) {
        log.info("Received request to get users: {}", request);
        if (request == null) {
            request = new GetUsersRequestDto();
        }
        ApiResponseDto response = userService.getUsers(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete_user")
    public ResponseEntity<ApiResponseDto> deleteUser(@RequestBody DeleteUserRequestDto request) {
        log.info("Received request to delete user: {}", request);
        ApiResponseDto response = userService.deleteUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update_user")
    public ResponseEntity<ApiResponseDto> updateUser(@RequestBody UpdateUserRequestDto request) {
        log.info("Received request to update users: {}", request);
        ApiResponseDto response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }
}