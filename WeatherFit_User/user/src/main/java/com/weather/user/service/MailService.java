package com.weather.user.service;

import com.weather.user.entity.MailCode;
import com.weather.user.entity.User;
import com.weather.user.repository.MailCodeRepository;
import com.weather.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {
    private final UserRepository userRepository;

    private final MailCodeRepository mailCodeRepository;

    private final JavaMailSender emailSender;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    private void sendEmail(String toEmail,
                          String title,
                          String text) throws Exception {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);

        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new Exception(e);
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    public void sendCodeToEmail(String toEmail) throws Exception {
        this.checkDuplicatedEmail(toEmail);
        String title = "옷늘날씨 이메일 인증 번호";
        String authCode = this.createCode();
        sendEmail(toEmail, title, authCode);

        Optional<MailCode> optionalMailCode = mailCodeRepository.findByEmail(toEmail);
        MailCode mailCode;
        log.info("optionalMailCode: ", optionalMailCode);

        if(optionalMailCode.isEmpty()) {
            mailCode = MailCode.builder()
                    .email(toEmail)
                    .code(authCode).build();

        } else {
            mailCode = optionalMailCode.get();
            mailCode.changeCode(authCode);

        }
        mailCodeRepository.save(mailCode);
    }

    private void checkDuplicatedEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email, false);
        if (optionalUser.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new Exception("이미 존재하는 회원입니다.");
        }
    }

    private String createCode() throws Exception {
        int codeLength = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < codeLength; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new Exception();
        }
    }

    public boolean verifiedCode(String email, String authCode) throws Exception {
        this.checkDuplicatedEmail(email);
        Optional<MailCode> optionalMailCode = mailCodeRepository.findByEmail(email);
        if(optionalMailCode.isPresent()) {
            MailCode mailCode = optionalMailCode.get();

            if(mailCode.getCode().equals(authCode)) {
                log.info("인증 성공");
                return true;
            } else  {
                log.info("인증 실패");
                return false;
            }
        } else {
            throw new Exception("인증 코드가 발급된 이메일이 아닙니다.");
        }
    }
}
