package com.example.bidMarket.service.impl;

import com.example.bidMarket.AWS.AmazonS3Service;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.Profile;
import com.example.bidMarket.repository.ProductImageRepository;
import com.example.bidMarket.repository.ProductRepository;
import com.example.bidMarket.repository.ProfileRepository;
import com.example.bidMarket.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    public final AmazonS3Service amazonS3Service;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProfileRepository profileRepository;


    @Override
    public void deleteAllProductImages(UUID productId) {

    }

    @Override
    public void deleteProductImages(UUID productId, UUID imageId) {

    }

    @Override
    public List<String> getProductImageUrls(UUID productId) {
        return List.of();
    }

    @Override
    public void setPrimaryProductImage(UUID productId, UUID imageId) {

    }

    // User Avatar methods
    @Override
    @Transactional
    public String uploadUserAvatar(UUID userId, MultipartFile avatar) throws Exception {
        Profile userProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Delete the old avatar from S3 if it exists
        if (userProfile.getProfileImageUrl() != null && !userProfile.getProfileImageUrl().equals(amazonS3Service.getDefaultUrl("users"))) {
            amazonS3Service.deleteImageOnS3(userProfile.getProfileImageUrl());
        }

        // Get the new avatar URL from S3
        String avatarUrl = amazonS3Service.handleUpload(userId, avatar, "users");

        // Update the user's profile image URL
        userProfile.setProfileImageUrl(avatarUrl);
        profileRepository.save(userProfile);
        log.info("Successfully updated profile image URL for user: {}", userId);

        return avatarUrl;
    }


    @Override
    @Transactional
    public List<ProductImage> uploadProductImages(UUID productId, List<MultipartFile> imageFiles, int indexPrimaryImage) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        List<ProductImage> productImages = new ArrayList<>();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            int i = 0;
            for (MultipartFile file : imageFiles) {
                String imageUrl = amazonS3Service.handleUpload(productId, file, "products");

                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImageUrl(imageUrl);
                productImage.setPrimary(i == indexPrimaryImage);  // Set the first image as primary

                productImageRepository.save(productImage);
                productImages.add(productImage);
                i++;
            }
        } else {
            // Use default image if no images are uploaded
            ProductImage defaultImage = new ProductImage();
            defaultImage.setProduct(product);
            defaultImage.setImageUrl(amazonS3Service.getDefaultUrl("products"));
            defaultImage.setPrimary(true);  // Default image should be primary

            productImageRepository.save(defaultImage);
            productImages.add(defaultImage);
        }

        return productImages;
    }


    @Override
    @Transactional
    public void deleteUserAvatar(UUID userId) throws IllegalAccessException {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        amazonS3Service.deleteImageOnS3(profile.getProfileImageUrl()); // delete previous image on S3
        profile.setProfileImageUrl(amazonS3Service.getDefaultUrl("users")); // setup default avatar for user
        profileRepository.save(profile);
    }

    @Override
    public String getUserAvatarUrl(UUID userId) {
        return "";
    }



}
