package com.example.bidMarket.service;

import com.example.bidMarket.model.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    String uploadImage(MultipartFile file, String folder) throws IOException;
    void deleteImage(String imageUrl) throws IOException;
    String getImageUrl(String fileName);
    String uploadUserAvatar(UUID userId, MultipartFile avatar) throws Exception;
    List<String> uploadProductImages(UUID productId, List<MultipartFile> imageFiles) throws Exception;
    void deleteUserAvatar(UUID userId) throws Exception;
}