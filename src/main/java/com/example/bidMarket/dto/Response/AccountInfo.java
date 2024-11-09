package com.example.bidMarket.dto.Response;

import com.example.bidMarket.Enum.AddressType;
import com.example.bidMarket.Enum.Role;
import lombok.*;

import java.util.UUID;
@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    UUID userId;
    String email;
    String fullName;
    Role role;
    String phoneNumber;
    String avatarImageUrl;
    private AddressType addressType;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
