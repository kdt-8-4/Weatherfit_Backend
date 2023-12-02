package com.weatherfit.board.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.weatherfit.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageService {
    @Autowired
    ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public String saveImage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 이미지의 해시값을 생성합니다.
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] imageBytes = file.getBytes();
            byte[] digest = md.digest(imageBytes);
            String imageHash = new BigInteger(1, digest).toString(16);

            // 이미지 이름에 해시값을 사용합니다.
            String fileName = imageHash + fileExtension;
            String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

            // 파일의 내용이 변경되지 않았는지 확인합니다.
            if (!imageRepository.existsByImageUrl(fileUrl)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            }

            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }
}