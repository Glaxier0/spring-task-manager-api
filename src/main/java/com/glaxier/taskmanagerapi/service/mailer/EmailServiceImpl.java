package com.glaxier.taskmanagerapi.service.mailer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String SENDER;
    private String WELCOME_SUBJECT = "Thanks for joining in!";
    private String WELCOME_TEXT = "Welcome to the app, name. Let me know how you get along with the app.";
    private String CANCELLATION_SUBJECT = "Sorry to see you go!";
    private String CANCELLATION_TEXT = "Goodbye, name. Hope to see you back sometime soon.";

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendWelcomeEmail(String name, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(email);
        message.setSubject(WELCOME_SUBJECT);
        message.setText(WELCOME_TEXT.replace("name", name));
        emailSender.send(message);
    }

    @Override
    public void sendCancellationEmail(String name, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(email);
        message.setSubject(CANCELLATION_SUBJECT);
        message.setText(CANCELLATION_TEXT.replace("name", name));
        emailSender.send(message);
    }
}

