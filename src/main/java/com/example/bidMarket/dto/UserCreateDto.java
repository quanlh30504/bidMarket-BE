package com.example.bidMarket.dto;

import com.example.bidMarket.model.Role;
import lombok.Data;

@Data
public class UserCreateDto {
    private String email;
    private String password;
    private Role role;
}
