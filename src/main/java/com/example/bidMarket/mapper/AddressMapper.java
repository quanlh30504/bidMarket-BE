package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.AddressDto;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.Address;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    private final UserRepository userRepository;
    public Address addressDtoToAddress(AddressDto addressDto){
        User user = userRepository.findById(addressDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return Address.builder()
                .user(user)
//                .addressType(addressDto.getAddressType())
                .streetAddress(addressDto.getStreetAddress())
//                .state(addressDto.getState())
//                .postalCode(addressDto.getPostalCode())
                .city(addressDto.getCity())
                .country(addressDto.getCountry())
                .build();
    }

    public AddressDto addressToAddressDto(Address address){
        return AddressDto.builder()
//                .id(address.getId())
                .userId(address.getUser().getId())
//                .addressType(address.getAddressType())
                .streetAddress(address.getStreetAddress())
                .city(address.getCity())
//                .postalCode(address.getPostalCode())
//                .state(address.getState())
                .country(address.getCountry())
                .build();
    }

}
