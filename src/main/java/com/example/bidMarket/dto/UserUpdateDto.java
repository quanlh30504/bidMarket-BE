package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.Role;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String email;
    private Role role;
    private String password;
    private boolean isBanned;
}
