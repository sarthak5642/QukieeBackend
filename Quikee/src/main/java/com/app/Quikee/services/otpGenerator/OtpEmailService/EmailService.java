package com.app.Quikee.services.otpGenerator.OtpEmailService;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOtpEmail(String to, String subject, String otp) throws MessagingException;
}
