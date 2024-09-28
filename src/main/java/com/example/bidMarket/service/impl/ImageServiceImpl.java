package com.example.bidMarket.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.bidMarket.model.Profile;
import com.example.bidMarket.repository.ProductImageRepository;
import com.example.bidMarket.repository.ProfileRepository;
import com.example.bidMarket.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
@Service
public class ImageServiceImpl implements ImageService {

    private final AmazonS3 amazonS3Client;
    private final ProductImageRepository productImageRepository;
    private final ProfileRepository profileRepository;
    private final String bucketName;

    public ImageServiceImpl(AmazonS3 amazonS3Client, ProductImageRepository productImageRepository, ProfileRepository profileRepository,@Value("${aws.s3.bucket}") String bucketName) {
        this.amazonS3Client = amazonS3Client;
        this.productImageRepository = productImageRepository;
        this.profileRepository = profileRepository;
        this.bucketName = bucketName;
    }


    @Override
    public List<String> uploadProductImages(UUID productId, List<MultipartFile> images, boolean setPrimary) throws IOException {
        return List.of();
    }

    @Override
    public void deleteProductImages(UUID productId, UUID imageId) {

    }

    @Override
    public void deleteAllProductImages(UUID productId) {

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
    public String uploadUserAvatar(UUID userId, MultipartFile avatar) throws IOException {
        String fileName = generateFileName(avatar);
        String s3Key = "users/" + userId.toString() + "/avatar/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(avatar.getSize());
        metadata.setContentType(avatar.getContentType());

        amazonS3Client.putObject(new PutObjectRequest(bucketName, s3Key, avatar.getInputStream(), metadata));
        String avatarUrl = amazonS3Client.getUrl(bucketName, s3Key).toString();

        Profile userProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userProfile.setProfileImageUrl(avatarUrl);
        profileRepository.save(userProfile);

        return avatarUrl;
    }

    @Override
    public void deleteUserAvatar(UUID userId) {

    }

    @Override
    public String getUserAvatarUrl(UUID userId) {
        return "";
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
