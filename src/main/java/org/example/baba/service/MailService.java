package org.example.baba.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 가입승인 메일 전송
    public void sendEmail(String email, String randomCode) {
        MimeMessage mimeMessage = createMessage(email, randomCode);
        mailSender.send(mimeMessage);
    }

    // 메일 내용 생성
    public MimeMessage createMessage(String email, String randomCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 받는 이메일 설정
            mimeMessageHelper.setTo(email);
            // 보내는 이메일 설정
            mimeMessageHelper.setFrom(senderEmail);
            // 메일 제목 설정
            mimeMessageHelper.setSubject("가입승인 코드");
            // 메일 본문 작성
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + randomCode + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            // 메일 본문 설정
            mimeMessageHelper.setText(body, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return mimeMessage;
    }
}
