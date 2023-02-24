package com.witchs.blog.email;

import com.witchs.blog.security.TokenCreator;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final TokenCreator tokenCreator;

    public void sendVerificationEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("witch.blog.pwr@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Potwierdż swój email");
        message.setText(createVerificationLink(toEmail));
        javaMailSender.send(message);
    }

    public void sendPasswordRecoveryEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("witch.blog.pwr@gmail.com");
        message.setTo(email);
        message.setSubject("Zmień hasło poprzez link w treści");
        message.setText(tokenCreator.createPasswordRecoveryToken(email));
        javaMailSender.send(message);
    }

    private String createVerificationLink(String email) {
        String token = tokenCreator.createEmailConfirmationToken(email);
        return "http://localhost:8080/auth/email/confirmation?token=" + token;
    }


}
