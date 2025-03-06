package com.example.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private UUID userId;
    
    private UUID managerId;
    
    private String fullName;
    
    private String mobNum;
    
    private String panNum;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private boolean isActive;
}