package com.example.bidMarket.AWS;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {
    private final AmazonS3 amazonS3Client;

    @Value("${aws.default_avatar_url}")
    private String DEFAULT_AVATAR_URL;

    @Value("${aws.default_product_url}")
    private String DEFAULT_PRODUCT_URL;


    @Value("${aws.s3.bucket}")
    private String bucketName;

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public void deleteImageOnS3(String image) {
        // Extract the S3 key from the URL
        String s3Key = image.substring(image.indexOf(".com/") + 5);  // Assuming URL follows the pattern
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, s3Key));
        log.info("Deleted old avatar from S3: {}", image);
    }

    public String handleUpload(UUID entityId, MultipartFile imageFile, String entityType) throws IllegalAccessException {
        String imageUrl;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = generateFileName(imageFile);
                String s3Key = entityType + "/" + entityId.toString() + "/images/" + fileName;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(imageFile.getSize());
                metadata.setContentType(imageFile.getContentType());

                amazonS3Client.putObject(new PutObjectRequest(bucketName, s3Key, imageFile.getInputStream(), metadata));
                log.info("Successfully uploaded avatar for {}: {}", entityType,entityId);
                imageUrl = amazonS3Client.getUrl(bucketName, s3Key).toString();
            }
            catch (AmazonServiceException e) {
                log.error("Error uploading image  to S3 for {}: {}", entityType,entityId, e);
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            } catch (IOException e) {
                log.error("Error reading image file input stream for {}: {}", entityType,entityId, e);
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        } else {
            imageUrl = getDefaultUrl(entityType);
        }
        return imageUrl;
    }

    public String getDefaultUrl(String entityType) throws IllegalAccessException {
        switch (entityType) {
            case "users":
                return DEFAULT_AVATAR_URL;
            case "products":
                return DEFAULT_PRODUCT_URL;
            default:
                throw new IllegalAccessException("Not existed entity type : " + entityType);

        }
    }

}
