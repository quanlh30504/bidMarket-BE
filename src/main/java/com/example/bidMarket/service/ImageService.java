package com.example.bidMarket.service;

import com.example.bidMarket.model.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    void deleteProductImages(UUID productId, UUID imageId);
    void deleteAllProductImages(UUID productId);
    List<String> getProductImageUrls(UUID productId);
    void setPrimaryProductImage(UUID productId, UUID imageId);

    String uploadUserAvatar(UUID userId, MultipartFile file) throws Exception;
    List<ProductImage> uploadProductImages(UUID productId, List<MultipartFile> imageFiles, int indexPrimaryImage) throws Exception;
    void deleteUserAvatar(UUID userId) throws Exception;
    String getUserAvatarUrl(UUID userId);

}
