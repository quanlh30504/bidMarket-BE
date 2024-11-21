package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.Role;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private Role role;
    private boolean isBanned;
    private boolean isVerified;
    private ProfileDto profile;
}
