package com.fluffypuppy.shop.service;

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

    public final String ePw = createKey();

    private MimeMessage createMessage(String to) throws Exception{
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);   // 보내는 대상
        message.setSubject("<FLUFFY PUPPY> 회원 가입 이메일 인증");  // 제목

        String msg = "";
        msg += "<div style='margin:20px; padding:20px; border:1px solid black;'>";
        msg += "<h2>안녕하세요. FLUFFY PUPPY 입니다.</h2>";
        msg += "<br>";
        msg += "<p>아래 코드를 복사해서 입력해주세요.</p>";
        msg += "<br>";
        msg += "<div font-family:verdana;'>";
        msg += "<h3 style='color:red;'>인증 코드</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "CODE : <strong style='color:red;'>";
        msg += ePw + "</strong><div><br/>";
        msg += "</div>";
        message.setText(msg, "utf-8", "html");  //내용
        message.setFrom(new InternetAddress("rhrnal000@naver.com", "회원가입 인증"));

        return message;

    }

    public static String createKey(){
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for(int i = 0; i < 8; i++){
            int index = random.nextInt(3);

            switch (index){
                case 0:
                    key.append((char) ((int)(random.nextInt(26)) + 97));
                    // a ~ z 소문자 (ex. 1 + 97 = 98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int)(random.nextInt(26)) + 65));
                    // A ~ Z 대문자
                case 2:
                    key.append((random.nextInt(10)));
                    //0~9
                    break;
            }
        }
        return key.toString();
    }

    public String sendSimpleMessage(String to) throws Exception{
        MimeMessage message = createMessage(to);
        try{
            emailSender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }

}
