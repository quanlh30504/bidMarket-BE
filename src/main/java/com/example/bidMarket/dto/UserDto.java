package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private Role role;
    private boolean isBanned;
    private ProfileDto profile;
}
