package com.example.usermanagement.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateUserRequestDto {
    private List<UUID> userIds;
    private UpdateDataDto updateData;
}