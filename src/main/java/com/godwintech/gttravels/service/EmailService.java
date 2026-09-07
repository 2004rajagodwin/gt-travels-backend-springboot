package com.godwintech.gttravels.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendEmail(String to, String subject, String body) {
        // Implement with JavaMailSender or external API
    }
}