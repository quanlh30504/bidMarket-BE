package com.example.bidMarket.controller;

import com.example.bidMarket.dto.AddressDto;
import com.example.bidMarket.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/updateOrCreate/{userId}")
    public ResponseEntity<AddressDto> updateOrCreate(@PathVariable UUID userId, @RequestBody AddressDto addressDto){
        return ResponseEntity.ok(addressService.updateOrCreateAddress(userId, addressDto));
    }


}
