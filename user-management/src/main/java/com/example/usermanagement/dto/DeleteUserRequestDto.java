package com.example.usermanagement.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteUserRequestDto {
    private UUID userId;
    private String mobNum;
}