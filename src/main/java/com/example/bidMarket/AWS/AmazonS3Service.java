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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {
    private final AmazonS3 amazonS3Client;

    @Value("${DEFAULT_AVATAR_URL}")
    private String DEFAULT_AVATAR_URL;

    @Value("${DEFAULT_PRODUCT_URL}")
    private String DEFAULT_PRODUCT_URL;


    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folder) {
        String fileName = generateFileName(file);
        String s3Key = folder + "/" + fileName;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3Client.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata));
            log.info("Successfully uploaded file to S3: {}", s3Key);
            return getFileUrl(s3Key);
        } catch (IOException e) {
            log.error("Error IOException???: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        } catch (AmazonServiceException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public void deleteFile(String fileUrl) {
        String s3Key = extractS3KeyFromUrl(fileUrl);
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, s3Key));
            log.info("Successfully deleted file from S3: {}", s3Key);
        } catch (AmazonServiceException e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }


    public String getFileUrl(String s3Key) {
        return amazonS3Client.getUrl(bucketName, s3Key).toString();
    }

    public String handleUpload(UUID entityId, MultipartFile file, String entityType) {
        if (file == null || file.isEmpty()) {
            return getDefaultUrl(entityType);
        }

        String folder = entityType + "/" + entityId + "/images";
        return uploadFile(file, folder);
    }


    public String getDefaultUrl(String entityType){
        switch (entityType) {
            case "users":
                return DEFAULT_AVATAR_URL;
            case "products":
                return DEFAULT_PRODUCT_URL;
            default:
                throw new IllegalArgumentException("Invalid entity type : " + entityType);

        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    private String extractS3KeyFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);
    }
}