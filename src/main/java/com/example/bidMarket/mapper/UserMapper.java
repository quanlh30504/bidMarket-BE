package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.*;
import com.example.bidMarket.model.Address;
import com.example.bidMarket.model.BidderProfile;
import com.example.bidMarket.model.SellerProfile;
import com.example.bidMarket.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
    User userCreateDtoToUser(UserCreateDto userCreateDto);
    void updateUserFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);

    @Mapping(target = "userId", source = "user.id")
    BidderProfileDto bidderProfileToBidderProfileDto(BidderProfile bidderProfile);

    @Mapping(target = "userId", source = "user.id")
    SellerProfileDto sellerProfileToSellerProfileDto(SellerProfile sellerProfile);

    @Mapping(target = "userId", source = "user.id")
    AddressDto addressToAddressDto(Address address);
}
