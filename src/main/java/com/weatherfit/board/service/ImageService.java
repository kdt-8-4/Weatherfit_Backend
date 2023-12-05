package com.weatherfit.board.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.weatherfit.board.domain.ImageEntity;
import com.weatherfit.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedNow = now.format(formatter);
            String fileName = formattedNow + "_weatherfit_" + originalFilename;
            String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

            if (!amazonS3Client.doesObjectExist(bucketName, fileName)) {
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


    public void deleteImage(ImageEntity imageEntity) {
        String imageUrl = imageEntity.getImageUrl();
//        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        amazonS3Client.deleteObject(bucketName, imageUrl);
    }

}



