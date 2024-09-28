package com.example.bidMarket.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductImageService {

    List<String> uploadProductImage(UUID productId, List<MultipartFile> images, boolean setPrimary) throws IOException;

    void deleteProductImage(UUID productId) throws IOException;

    List<String> getProductImageUrls(UUID productId);

    void setPrimaryImage(UUID productId, UUID imageId) throws IllegalArgumentException;
}
