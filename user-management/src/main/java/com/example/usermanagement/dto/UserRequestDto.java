package com.example.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRequestDto {
    @NotBlank(message = "Full name must not be empty")
    private String fullName;
    
    @Pattern(regexp = "^(\\+91|0)?[0-9]{10}$", message = "Mobile number must be a valid 10-digit number with optional prefix +91 or 0")
    private String mobNum;
    
    @Pattern(regexp = "^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$", message = "PAN number must be in format AABCP1234C")
    private String panNum;
    
    private UUID managerId;
}