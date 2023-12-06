package com.weather.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.weather.user.dto.GoogleUserDTO;
import com.weather.user.dto.UserDTO;
import com.weather.user.entity.User;
import com.weather.user.entity.UserRole;
import com.weather.user.repository.UserRepository;
import com.weather.user.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AmazonS3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public boolean verifyEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) {
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public boolean verifyNickname(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);

        if(optionalUser.isEmpty()) {
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public void signup(UserDTO userDTO) {
        log.info(userDTO);

        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isPresent()) {
            throw new Error("이미 존재하는 이메일입니다.");
        }

        optionalUser = userRepository.findByNickname(userDTO.getNickname());

        if(optionalUser.isPresent()) {
            throw new Error("이미 존재하는 닉네임입니다.");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setStatus(true);
        User user = dtoToEntity(userDTO);
        userRepository.save(user);
    }

    @Override
    public UserDTO googleUserCheck(GoogleUserDTO googleUserDTO) throws Exception{
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        String email = googleUserDTO.getEmail();
        String image = googleUserDTO.getPicture();
        Optional<User> optionalUser = userRepository.findByEmail(email, true);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO result = entityToDTO(user);
            result.setToken(jwtUtil.generateToken(user.getNickname()));
            return result;
        } else {
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("1234"))
                    .image(image)
                    .fromSocial(true)
                    .status(true)
                    .build();
            user.addRole(UserRole.USER);

            userRepository.save(user);

            UserDTO result = entityToDTO(user);
            return result;
        }
    }

    @Override
    public UserDTO googleUserAdditional(UserDTO userDTO) throws Exception{
        log.info("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail(), true);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.changeNickname(userDTO.getNickname());
            user.changeName(userDTO.getName());
            userRepository.save(user);
            UserDTO result = entityToDTO(user);
            result.setToken(jwtUtil.generateToken(user.getNickname()));
            return result;
        } else {
            throw new Exception("잘못 된 접근입니다.");
        }
    }

    @Override
    public UserDTO profile(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 유저입니다.");
        }
        User user = optionalUser.get();

        if(!user.isStatus()) {
            throw new Error("탈퇴 대기중인 유저입니다.");
        }

        UserDTO result = entityToDTO(user);
        return result;
    }

    @Override
    public UserDTO modify(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 유저입니다.");
        }
        User user = optionalUser.get();

        if(user.isStatus()) {
            if(userDTO.getPassword() != null) {
                user.changePassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            userRepository.save(user);

            UserDTO result = entityToDTO(user);
            return result;
        } else {
            throw new Error("이미 탈퇴 처리중인 유저입니다.");
        }
    }

    @Override
    public void saveImage(String email, MultipartFile image) {
        //실제 파일명에는 전체 경로가 들어오기 때문에 파일 이름만 추출
        String originalName = image.getOriginalFilename();
        log.info("originalName: " + image.getOriginalFilename());

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedNow = now.format(formatter);
        String fileName = formattedNow + "_weatherfit_" + originalName;
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        try {
            if (!s3Client.doesObjectExist(bucket, fileName)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(image.getContentType());
                metadata.setContentLength(image.getSize());
                s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);
            }

            log.info("email: " + email);
            Optional<User> optionalUser = userRepository.findByEmail(email, false);
            log.info("optionalUser: " + optionalUser);
            User user = optionalUser.get();
            user.changeImage(fileUrl);
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }

    @Override
    public void resetImage(String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user = optionalUser.get();
            user.changeImage(null);
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        log.info("optionalUser: " + optionalUser);

        if(optionalUser.isEmpty()) {
            throw new Error("존재하지 않는 유저입니다.");
        }
        User user = optionalUser.get();

        if(user.isStatus()) {
            user.changeStatus();
            userRepository.save(user);
        } else {
            throw new Error("이미 탈퇴 처리중인 유저입니다.");
        }
    }
}
