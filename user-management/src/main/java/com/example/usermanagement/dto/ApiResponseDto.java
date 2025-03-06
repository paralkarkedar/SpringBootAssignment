package com.example.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {
    private String message;
    private boolean success;
    private Object data;
    
    public static ApiResponseDto success(String message) {
        return new ApiResponseDto(message, true, null);
    }
    
    public static ApiResponseDto success(String message, Object data) {
        return new ApiResponseDto(message, true, data);
    }
    
    public static ApiResponseDto error(String message) {
        return new ApiResponseDto(message, false, null);
    }
}