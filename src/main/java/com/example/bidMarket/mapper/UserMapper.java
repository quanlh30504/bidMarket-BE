package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.*;
import com.example.bidMarket.model.Profile;
import com.example.bidMarket.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "profile", source = "profile")
    UserDto userToUserDto(User user);

    UserDto toDto(User user);

    @Mapping(target = "profile", ignore = true)
    void updateUserFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);

    ProfileDto profileToProfileDto(Profile profile);

    Profile profileDtoToProfile(ProfileDto profileDto);

    @Mapping(target = "user", ignore = true)
    void updateProfileFromDto(ProfileDto profileDto, @MappingTarget Profile profile);
}
