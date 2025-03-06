package com.example.usermanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "managers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
    @Id
    private UUID managerId;
    private String fullName;
    private String department;
    private LocalDateTime createdAt;
    private boolean isActive;
}