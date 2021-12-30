package com.glaxier.taskmanagerapi.service.mailer;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendWelcomeEmail(String name, String email);

    @Async
    void sendCancellationEmail(String name, String email);
}
