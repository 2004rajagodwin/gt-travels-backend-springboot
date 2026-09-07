package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.entity.User;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthEmailService {

    private static final Logger log = LoggerFactory.getLogger(AuthEmailService.class);

    private final JavaMailSender mailSender;
    private final AuthProperties authProperties;

    public AuthEmailService(JavaMailSender mailSender, AuthProperties authProperties) {
        this.mailSender = mailSender;
        this.authProperties = authProperties;
    }

    @Async
    public void sendVerificationEmail(User user, String rawToken) {
        try {
            String link = authProperties.getFrontendUrl()
                    + "/verify-email?token=" + rawToken;

            sendHtmlEmail(
                    user.getEmail(),
                    "Verify your GT Travels account",
                    """
                    <html><body>
                    <h2>Welcome to GT Travels</h2>
                    <p>Hi %s,</p>
                    <p>Please verify your email address by clicking the link below:</p>
                    <p><a href="%s">Verify Email</a></p>
                    <p>This link expires in %d hours.</p>
                    </body></html>
                    """.formatted(user.getName(), link, authProperties.getEmailVerificationHours())
            );
        } catch (Exception e) {
            log.error("Failed to send verification email to user {}", user.getId(), e);
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String rawToken) {
        try {
            String link = authProperties.getFrontendUrl()
                    + "/reset-password?token=" + rawToken;

            sendHtmlEmail(
                    user.getEmail(),
                    "Reset your GT Travels password",
                    """
                    <html><body>
                    <h2>Password Reset</h2>
                    <p>Hi %s,</p>
                    <p>Click the link below to reset your password:</p>
                    <p><a href="%s">Reset Password</a></p>
                    <p>This link expires in %d hour(s). If you did not request this, ignore this email.</p>
                    </body></html>
                    """.formatted(user.getName(), link, authProperties.getPasswordResetHours())
            );
        } catch (Exception e) {
            log.error("Failed to send password reset email to user {}", user.getId(), e);
        }
    }

    @Async
    public void sendPasswordChangedEmail(User user) {
        try {
            sendHtmlEmail(
                    user.getEmail(),
                    "Your GT Travels password was changed",
                    """
                    <html><body>
                    <h2>Password Changed</h2>
                    <p>Hi %s,</p>
                    <p>Your password was changed successfully. All active sessions were signed out.</p>
                    <p>If this was not you, contact support immediately.</p>
                    </body></html>
                    """.formatted(user.getName())
            );
        } catch (Exception e) {
            log.error("Failed to send password changed email to user {}", user.getId(), e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String html) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }
}
