package com.example.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDto {
    private List<UserResponseDto> users;
}