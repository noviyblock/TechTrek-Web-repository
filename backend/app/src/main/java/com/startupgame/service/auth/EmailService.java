package com.startupgame.service.auth;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendRegistrationEmail(String to, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            String subject = "Welcome to Startup Game!";
            String verificationLink = "https://techtrekgame.ru/api/auth/verify?token=" + token;
            String content = String.format(
                "Hi %s,\n\nPlease verify your email by clicking the link below:\n%s",
                username, verificationLink
                );


            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, false);

            mailSender.send(message);
            log.info("Registration email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send registration email to {}", to, e);
        }
    }
}
