package com.example.usermanagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usermanagement.dto.ApiResponseDto;
import com.example.usermanagement.dto.DeleteUserRequestDto;
import com.example.usermanagement.dto.GetUsersRequestDto;
import com.example.usermanagement.dto.UpdateDataDto;
import com.example.usermanagement.dto.UpdateUserRequestDto;
import com.example.usermanagement.dto.UserRequestDto;
import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.dto.UsersResponseDto;
import com.example.usermanagement.model.Manager;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.ManagerRepository;
import com.example.usermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;

    @Transactional
    public ApiResponseDto createUser(UserRequestDto requestDto) {
        try {
            // Validate fullName
            if (requestDto.getFullName() == null || requestDto.getFullName().trim().isEmpty()) {
                return ApiResponseDto.error("Full name must not be empty");
            }

            // Validate mobNum
            if (requestDto.getMobNum() == null || !requestDto.getMobNum().matches("^(\\+91|0)?[0-9]{10}$")) {
                return ApiResponseDto.error("Mobile number must be a valid 10-digit number with optional prefix +91 or 0");
            }

            // Format mobNum (remove prefix)
            String formattedMobNum = requestDto.getMobNum().replaceAll("^(\\+91|0)", "");

            // Validate panNum
            if (requestDto.getPanNum() == null || !requestDto.getPanNum().matches("^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$")) {
                return ApiResponseDto.error("PAN number must be in format AABCP1234C");
            }

            // Convert panNum to 
            String formattedPanNum = requestDto.getPanNum().toUpperCase();

            // Validate managerId if provided
            if (requestDto.getManagerId() != null) {
                Optional<Manager> managerOpt = managerRepository.findByManagerIdAndIsActiveTrue(requestDto.getManagerId());
                if (managerOpt.isEmpty()) {
                    return ApiResponseDto.error("Manager ID does not exist or is not active");
                }
            }

            // Check if user with this mobile number already exists
            Optional<User> existingUser = userRepository.findByMobNumAndIsActiveTrue(formattedMobNum);
            if (existingUser.isPresent()) {
                return ApiResponseDto.error("User with this mobile number already exists");
            }

            // Create new user
            User user = new User();
            user.setUserId(UUID.randomUUID());
            user.setFullName(requestDto.getFullName());
            user.setMobNum(formattedMobNum);
            user.setPanNum(formattedPanNum);
            user.setManagerId(requestDto.getManagerId());
            user.setCreatedAt(LocalDateTime.now());
            user.setActive(true);

            User savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getUserId());

            return ApiResponseDto.success("User created successfully", UserResponseDto.fromEntity(savedUser));
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ApiResponseDto.error("Error creating user: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponseDto getUsers(GetUsersRequestDto requestDto) {
        try {
            List<User> users;

            // If userId is provided, fetch by userId
            if (requestDto.getUserId() != null) {
                Optional<User> userOpt = userRepository.findById(requestDto.getUserId());
                users = userOpt.filter(User::isActive).map(List::of).orElse(Collections.emptyList());
            } 
            // If mobNum is provided, fetch by mobNum
            else if (requestDto.getMobNum() != null && !requestDto.getMobNum().trim().isEmpty()) {
                String formattedMobNum = requestDto.getMobNum().replaceAll("^(\\+91|0)", "");
                Optional<User> userOpt = userRepository.findByMobNumAndIsActiveTrue(formattedMobNum);
                users = userOpt.map(List::of).orElse(Collections.emptyList());
            } 
            // If managerId is provided, fetch all users with that manager
            else if (requestDto.getManagerId() != null) {
                users = userRepository.findByManagerIdAndIsActiveTrue(requestDto.getManagerId());
            } 
            // Otherwise, fetch all active users
            else {
                users = userRepository.findAllByIsActiveTrue();
            }

            List<UserResponseDto> userDtos = users.stream()
                    .map(UserResponseDto::fromEntity)
                    .collect(Collectors.toList());

            UsersResponseDto response = new UsersResponseDto(userDtos);
            return ApiResponseDto.success("Users fetched successfully", response);
        } catch (Exception e) {
            log.error("Error retrieving users", e);
            return ApiResponseDto.error("Error retrieving users: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponseDto deleteUser(DeleteUserRequestDto requestDto) {
        try {
            // Check if at least one field is provided
            if (requestDto.getUserId() == null && (requestDto.getMobNum() == null || requestDto.getMobNum().trim().isEmpty())) {
                return ApiResponseDto.error("Either userId or mobNum must be provided");
            }

            User user = null;

            // Find user by userId if provided
            if (requestDto.getUserId() != null) {
                Optional<User> userOpt = userRepository.findById(requestDto.getUserId());
                if (userOpt.isPresent() && userOpt.get().isActive()) {
                    user = userOpt.get();
                }
            } 
            // Find user by mobNum if provided and userId is not provided/found
            else if (requestDto.getMobNum() != null && !requestDto.getMobNum().trim().isEmpty()) {
                String formattedMobNum = requestDto.getMobNum().replaceAll("^(\\+91|0)", "");
                Optional<User> userOpt = userRepository.findByMobNumAndIsActiveTrue(formattedMobNum);
                if (userOpt.isPresent()) {
                    user = userOpt.get();
                }
            }

            // If user is not found, return error
            if (user == null) {
                return ApiResponseDto.error("User not found");
            }

            // Soft delete: set isActive to false
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            log.info("User deleted successfully with ID: {}", user.getUserId());
            return ApiResponseDto.success("User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting user", e);
            return ApiResponseDto.error("Error deleting user: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponseDto updateUser(UpdateUserRequestDto requestDto) {
        try {
            // Check if userIds and updateData are provided
            if (requestDto.getUserIds() == null || requestDto.getUserIds().isEmpty()) {
                return ApiResponseDto.error("User IDs must be provided");
            }
            if (requestDto.getUpdateData() == null) {
                return ApiResponseDto.error("Update data must be provided");
            }

            UpdateDataDto updateData = requestDto.getUpdateData();
            
            // For bulk update (multiple users), only managerId updates are allowed
            if (requestDto.getUserIds().size() > 1) {
                // Check if any field other than managerId is being updated
                if (updateData.getFullName() != null || updateData.getMobNum() != null || updateData.getPanNum() != null) {
                    return ApiResponseDto.error("Bulk update is only allowed for managerId updates");
                }
                
                // Validate managerId if provided
                if (updateData.getManagerId() == null) {
                    return ApiResponseDto.error("Manager ID must be provided for bulk update");
                }
                
                Optional<Manager> managerOpt = managerRepository.findByManagerIdAndIsActiveTrue(updateData.getManagerId());
                if (managerOpt.isEmpty()) {
                    return ApiResponseDto.error("Manager ID does not exist or is not active");
                }
            }

            // Validate mobile number format if provided
            String formattedMobNum = null;
            if (updateData.getMobNum() != null) {
                if (!updateData.getMobNum().matches("^(\\+91|0)?[0-9]{10}$")) {
                    return ApiResponseDto.error("Mobile number must be a valid 10-digit number with optional prefix +91 or 0");
                }
                formattedMobNum = updateData.getMobNum().replaceAll("^(\\+91|0)", "");
            }

            // Validate PAN number format if provided
            String formattedPanNum = null;
            if (updateData.getPanNum() != null) {
                if (!updateData.getPanNum().matches("^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$")) {
                    return ApiResponseDto.error("PAN number must be in format AABCP1234C");
                }
                formattedPanNum = updateData.getPanNum().toUpperCase();
            }

            // Validate manager ID if provided
            if (updateData.getManagerId() != null) {
                Optional<Manager> managerOpt = managerRepository.findByManagerIdAndIsActiveTrue(updateData.getManagerId());
                if (managerOpt.isEmpty()) {
                    return ApiResponseDto.error("Manager ID does not exist or is not active");
                }
            }

            List<String> notFoundUserIds = new ArrayList<>();
            List<User> updatedUsers = new ArrayList<>();

            for (UUID userId : requestDto.getUserIds()) {
                Optional<User> userOpt = userRepository.findById(userId);
                
                if (userOpt.isEmpty() || !userOpt.get().isActive()) {
                    notFoundUserIds.add(userId.toString());
                    continue;
                }
                
                User user = userOpt.get();
                
                // Handle manager update differently (as per requirements)
                if (updateData.getManagerId() != null) {
                    // If user already has a manager
                    if (user.getManagerId() != null) {
                        // Deactivate current user
                        user.setActive(false);
                        user.setUpdatedAt(LocalDateTime.now());
                        userRepository.save(user);
                        
                        // Create new user with updated manager
                        User newUser = new User();
                        newUser.setUserId(UUID.randomUUID());
                        newUser.setFullName(user.getFullName());
                        newUser.setMobNum(user.getMobNum());
                        newUser.setPanNum(user.getPanNum());
                        newUser.setManagerId(updateData.getManagerId());
                        newUser.setCreatedAt(LocalDateTime.now());
                        newUser.setActive(true);
                        
                        user = userRepository.save(newUser);
                    } else {
                        // First time manager assignment
                        user.setManagerId(updateData.getManagerId());
                        user.setUpdatedAt(LocalDateTime.now());
                        user = userRepository.save(user);
                    }
                } else {
                    // Regular field updates
                    if (updateData.getFullName() != null && !updateData.getFullName().trim().isEmpty()) {
                        user.setFullName(updateData.getFullName());
                    }
                    
                    if (formattedMobNum != null) {
                        user.setMobNum(formattedMobNum);
                    }
                    
                    if (formattedPanNum != null) {
                        user.setPanNum(formattedPanNum);
                    }
                    
                    user.setUpdatedAt(LocalDateTime.now());
                    user = userRepository.save(user);
                }
                
                updatedUsers.add(user);
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("updatedUsers", updatedUsers.stream().map(UserResponseDto::fromEntity).collect(Collectors.toList()));
            
            if (!notFoundUserIds.isEmpty()) {
                responseData.put("notFoundUserIds", notFoundUserIds);
                return ApiResponseDto.success("Some users were updated, but some were not found", responseData);
            }
            
            return ApiResponseDto.success("Users updated successfully", responseData);
        } catch (Exception e) {
            log.error("Error updating users", e);
            return ApiResponseDto.error("Error updating users: " + e.getMessage());
        }
    }
}