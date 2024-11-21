package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.UserUpdateDto;
import com.example.bidMarket.model.User;
import com.example.bidMarket.dto.UserDto;
import com.example.bidMarket.dto.ProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.bidMarket.model.Profile;

@Component
public class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Convert User to UserDto
    public UserDto userToUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setBanned(user.isBanned());
        userDto.setVerified(user.isVerified());

        if (user.getProfile() != null) {
            userDto.setProfile(profileToProfileDto(user.getProfile()));
        }

        return userDto;
    }

    // Convert Profile to ProfileDto
    public ProfileDto profileToProfileDto(Profile profile) {
        if (profile == null) {
            return null;
        }

        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setFullName(profile.getFullName());
        profileDto.setPhoneNumber(profile.getPhoneNumber());
        profileDto.setProfileImageUrl(profile.getProfileImageUrl());

        return profileDto;
    }

    // Convert ProfileDto to Profile
    public Profile profileDtoToProfile(ProfileDto profileDto) {
        if (profileDto == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setId(profileDto.getId());
        profile.setFullName(profileDto.getFullName());
        profile.setPhoneNumber(profileDto.getPhoneNumber());
        profile.setProfileImageUrl(profileDto.getProfileImageUrl());

        return profile;
    }

    // Update User entity from UserUpdateDto
    public void updateUserFromDto(UserUpdateDto userUpdateDto, User user) {
        if (userUpdateDto == null || user == null) {
            return;
        }

        user.setEmail(userUpdateDto.getEmail());
        user.setRole(userUpdateDto.getRole());
        user.setPasswordHash(passwordEncoder.encode(userUpdateDto.getPassword()));
        user.setBanned(userUpdateDto.isBanned());
    }

}
