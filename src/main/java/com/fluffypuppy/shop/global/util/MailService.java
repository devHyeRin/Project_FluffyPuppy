package com.fluffypuppy.shop.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    /**
     * 인증 메일 발송
     */
    public String sendSimpleMessage(String to) throws Exception {

        // ✅ 메일 요청마다 인증 코드 새로 생성
        String authCode = createKey();

        MimeMessage message = createMessage(to, authCode);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메일 발송 실패: " + e.getMessage());
        }

        return authCode;
    }

    /**
     * 메일 내용 생성
     */
    private MimeMessage createMessage(String to, String authCode) throws Exception {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("[FLUFFY PUPPY] 회원가입 이메일 인증");

        StringBuilder msg = new StringBuilder();
        msg.append("<div style='margin:20px; padding:20px; border:1px solid #ddd;'>");
        msg.append("<h2>안녕하세요, FLUFFY PUPPY 입니다 🐾</h2>");
        msg.append("<p>아래 인증 코드를 입력해 주세요.</p>");
        msg.append("<div style='margin-top:20px;'>");
        msg.append("<h3 style='color:#fd7996;'>인증 코드</h3>");
        msg.append("<strong style='font-size:24px;'>").append(authCode).append("</strong>");
        msg.append("</div>");
        msg.append("</div>");

        message.setText(msg.toString(), "utf-8", "html");

        // ✅ 네이버 SMTP 안정 설정 (영문 From)
        message.setFrom(new InternetAddress("rhrnal000@naver.com", "FLUFFY PUPPY"));

        return message;
    }

    /**
     * 인증 코드 생성 (8자리)
     */
    private String createKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0:
                    // a ~ z
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // A ~ Z
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 0 ~ 9
                    key.append(random.nextInt(10));
                    break;
            }
        }
        return key.toString();
    }
}
