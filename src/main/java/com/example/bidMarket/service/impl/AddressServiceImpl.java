package com.example.bidMarket.service.impl;

import com.example.bidMarket.dto.AddressDto;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.AddressMapper;
import com.example.bidMarket.model.Address;
import com.example.bidMarket.repository.AddressRepository;
import com.example.bidMarket.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDto updateOrCreateAddress(UUID userId, AddressDto addressDto) {
        Address address = addressRepository.findByUserId(userId).orElse(null);
        if (address == null){
            return createAddress(addressDto);
        }else{
//            address.setAddressType(addressDto.getAddressType());
            address.setCity(addressDto.getCity());
            address.setCountry(addressDto.getCountry());
//            address.setState(addressDto.getState());
//            address.setPostalCode(addressDto.getPostalCode());
            address.setStreetAddress(addressDto.getStreetAddress());
            return addressMapper.addressToAddressDto(addressRepository.save(address));
        }

    }

    @Override
    @Transactional
    public AddressDto createAddress(AddressDto addressDto) {
        Address address = addressMapper.addressDtoToAddress(addressDto);
        return addressMapper.addressToAddressDto(addressRepository.save(address));
    }
}
