package com.example.bidMarket.controller;

import com.example.bidMarket.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) {
        try {
            String imageUrl = imageService.uploadImage(file, folder);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload image" + e.getMessage());
        }
    }

    @PostMapping("/upload/product/{productId}")
    public ResponseEntity<List<String>> uploadProductImages(
            @PathVariable UUID productId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> imageUrls = imageService.uploadProductImages(productId, files);
            return ResponseEntity.ok(imageUrls);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/upload/avatar/{userId}")
    public ResponseEntity<String> uploadUserAvatar(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String avatarUrl = imageService.uploadUserAvatar(userId, file);
            return ResponseEntity.ok(avatarUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload avatar: " + e.getMessage());
        }
    }

    @DeleteMapping("/avatar/{userId}")
    public ResponseEntity<Void> deleteUserAvatar(@PathVariable UUID userId) {
        try {
            imageService.deleteUserAvatar(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
