package com.example.usermanagement.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GetUsersRequestDto {
    private UUID userId;
    private String mobNum;
    private UUID managerId;
}