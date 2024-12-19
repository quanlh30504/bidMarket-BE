package com.example.bidMarket.AWS;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.bidMarket.dto.Response.PreSignedUrlResponse;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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

    public PreSignedUrlResponse generatePreSignedUrl(String folder, String fileType) {
        try {
            String fileName = generateFileName(fileType);
            String s3Key = folder + "/" + fileName;

            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 5; // 5 minutes
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, s3Key)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration)
                            .withContentType(fileType);

            URL preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

            String finalUrl = getFileUrl(s3Key);
            log.info("PreSignedUrlResponse: uploadUrl={}, fileUrl={}", preSignedUrl.toString(), finalUrl);
            return new PreSignedUrlResponse(preSignedUrl.toString(), finalUrl);
        } catch (AmazonServiceException e) {
            log.error("Error generating pre-signed URL: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public String uploadFile(MultipartFile file, String folder) {
        String fileName = generateFileName(String.valueOf(file));
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
            throw new AppException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    public void deleteFolder(String folderPrefix) {
        try {
            ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                    .withBucketName(bucketName)
                    .withPrefix(folderPrefix); // Prefix của "folder"

            ListObjectsV2Result result;
            do {
                result = amazonS3Client.listObjectsV2(listObjectsRequest);

                List<DeleteObjectsRequest.KeyVersion> keysToDelete = result.getObjectSummaries().stream()
                        .map(S3ObjectSummary::getKey)
                        .map(DeleteObjectsRequest.KeyVersion::new)
                        .collect(Collectors.toList());

                if (!keysToDelete.isEmpty()) {
                    DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
                            .withKeys(keysToDelete);
                    amazonS3Client.deleteObjects(deleteObjectsRequest);
                    log.info("Successfully deleted {} objects in folder: {}", keysToDelete.size(), folderPrefix);
                }

                listObjectsRequest.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());
        } catch (Exception e) {
            log.error("Error deleting folder from S3: {}", e.getMessage());
            throw new AppException(ErrorCode.FOLDER_DELETE_FAILED);
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

    private String generateFileName(String contentType) {
        String extension = contentType.substring(contentType.lastIndexOf('/') + 1);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String extractS3KeyFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);
    }
}
