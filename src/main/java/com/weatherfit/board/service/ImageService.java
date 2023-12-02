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
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String originalFilename = file.getOriginalFilename();
            String fileName = timestamp + "_" + originalFilename;
            String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

            // 파일명이 이미 존재하는지 확인
            if (amazonS3Client.doesObjectExist(bucketName, fileName)) {
                // 파일명이 존재한다면, 고유한 UUID를 추가하여 새로운 파일명 생성
                fileName = timestamp + "_" + UUID.randomUUID().toString() + "_" + originalFilename;
                fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            }

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }
}



