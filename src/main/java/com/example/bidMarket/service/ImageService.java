package com.example.bidMarket.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    List<String> uploadProductImages(UUID productId, List<MultipartFile> images, boolean setPrimary) throws IOException;
    void deleteProductImages(UUID productId, UUID imageId);
    void deleteAllProductImages(UUID productId);
    List<String> getProductImageUrls(UUID productId);
    void setPrimaryProductImage(UUID productId, UUID imageId);

    String uploadUserAvatar(UUID userId, MultipartFile file) throws IOException;
    void deleteUserAvatar(UUID userId);
    String getUserAvatarUrl(UUID userId);

}
