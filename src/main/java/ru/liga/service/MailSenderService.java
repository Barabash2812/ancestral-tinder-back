package ru.liga.service;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class MailSenderService {

    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String username;

    public MailSenderService(@Qualifier("javaMailSenderBean") JavaMailSender sender) {
        this.sender = sender;
    }

    public void send(String email, String theme, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(email);
        mailMessage.setSubject(theme);
        mailMessage.setText(message);
        sender.send(mailMessage);
    }
}

