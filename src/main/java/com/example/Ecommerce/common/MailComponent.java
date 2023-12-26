package com.example.Ecommerce.common;

import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailComponent {

  private static final String fromName = "ZB 관리자";
  private final JavaMailSender javaMailSender;
  private final SpringTemplateEngine templateEngine;
  @Value("${spring.mail.username}")
  private String fromEmail;

  private SecretKey secretKey;
  private Cipher cipher;

  @PostConstruct
  public void init() {
    try {
      cipher = Cipher.getInstance("AES");
      secretKey = KeyGenerator.getInstance("AES").generateKey();
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  public void sendVerifyLink(Long id, String toEmail, String toName) {
    String title = "[ZB E-Commerce] 회원가입을 위해 메일을 인증해 주세요.";
    String verification_url = "http://localhost:8080/api/user/verify/" + id;

    Context context = new Context();
    context.setVariable("name", toName);
    context.setVariable("verification_url", verification_url);

    String contents = templateEngine.process("registerUser", context);

    MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      InternetAddress from = addressFromTo(fromEmail, fromName);

      InternetAddress to = addressFromTo(toEmail, toName);

      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(title);
      mimeMessageHelper.setText(contents, true);
    };
    try {
      javaMailSender.send(mimeMessagePreparator);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  private InternetAddress addressFromTo(String email, String name) {
    InternetAddress address = new InternetAddress();
    try {
      address.setAddress(email);
      address.setPersonal(name);
    } catch (UnsupportedEncodingException e) {
      log.info(e.getMessage());
    }
    return address;
  }

  public void sendUserId(String userId, String toEmail, String toName) {
    String title = "[ZB E-Commerce] 요청하신 아이디를 확인해주세요.";

    Context context = new Context();
    context.setVariable("userId", userId);

    String contents = templateEngine.process("findUserId", context);

    MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      InternetAddress from = addressFromTo(fromEmail, fromName);

      InternetAddress to = addressFromTo(toEmail, toName);

      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(title);
      mimeMessageHelper.setText(contents, true);
    };
    try {
      javaMailSender.send(mimeMessagePreparator);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  public void sendResetPasswordForm(String userId, String toEmail, String toName)
      throws Exception {
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(userId.getBytes());
    String encryptedUserId = Base64.getEncoder().encodeToString(encryptedBytes);
    encryptedUserId = encryptedUserId.replace("/", "");

    String title = "[ZB E-Commerce] 비밀번호를 재설정해주세요.";
    String resetPasswordForm_url =
        "http://localhost:8080/api/user/reset/password/" + encryptedUserId;

    Context context = new Context();
    context.setVariable("name", toName);
    context.setVariable("resetPasswordForm_url", resetPasswordForm_url);

    String contents = templateEngine.process("findUserPassword", context);

    MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      InternetAddress from = addressFromTo(fromEmail, fromName);

      InternetAddress to = addressFromTo(toEmail, toName);

      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(title);
      mimeMessageHelper.setText(contents, true);
    };
    try {
      javaMailSender.send(mimeMessagePreparator);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  public String decryptUserId(String encryptedUserId) throws Exception {
    byte[] encryptedBytes = Base64.getDecoder().decode(encryptedUserId.getBytes());
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

    return new String(decryptedBytes);
  }
}
