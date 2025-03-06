package com.example.usermanagement.dto;

import com.example.usermanagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID userId;
    private UUID managerId;
    private String fullName;
    private String mobNum;
    private String panNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    
    public static UserResponseDto fromEntity(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(user.getUserId());
        dto.setManagerId(user.getManagerId());
        dto.setFullName(user.getFullName());
        dto.setMobNum(user.getMobNum());
        dto.setPanNum(user.getPanNum());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setActive(user.isActive());
        return dto;
    }
}