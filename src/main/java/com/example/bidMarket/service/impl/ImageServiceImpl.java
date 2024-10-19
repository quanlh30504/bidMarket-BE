package com.example.bidMarket.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final AmazonS3Service amazonS3Service;
    private final ProfileRepository profileRepository;

    @Override
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        return amazonS3Service.uploadFile(file, folder);
    }

    @Override
    public void deleteImage(String imageUrl) throws IOException {
        amazonS3Service.deleteFile(imageUrl);
    }

    @Override
    public String getImageUrl(String fileName) {
        return amazonS3Service.getFileUrl(fileName);
    }

    @Override
    @Transactional
    public String uploadUserAvatar(UUID userId, MultipartFile avatar) throws Exception {
        Profile userProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        if (userProfile.getProfileImageUrl() != null) {
            amazonS3Service.deleteFile(userProfile.getProfileImageUrl());
        }

        String avatarUrl = amazonS3Service.handleUpload(userId, avatar, "users");
        userProfile.setProfileImageUrl(avatarUrl);
        profileRepository.save(userProfile);
        log.info("Successfully updated profile image for user: {}", userId);

        return avatarUrl;
    }

    @Override
    @Transactional
    public List<String> uploadProductImages(UUID productId, List<MultipartFile> imageFiles) throws Exception {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : imageFiles) {
            String imageUrl = amazonS3Service.handleUpload(productId, file, "products");
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }

    @Override
    @Transactional
    public void deleteUserAvatar(UUID userId) throws Exception {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        amazonS3Service.deleteFile(profile.getProfileImageUrl());
        profile.setProfileImageUrl(amazonS3Service.getDefaultUrl("users"));
        profileRepository.save(profile);
    }
}