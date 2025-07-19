package com.app.Quikee.serviceImpl.otpGeneratorImpl;

import com.app.Quikee.services.otpGenerator.OtpEmailService.EmailService;
import com.app.Quikee.services.otpGenerator.OtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
public class OtpServiceImpl implements OtpService {

    EmailService emailService;

    @Value("${otp.subject}")
    private static final String otpMailSubject = new String();

    public OtpServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int EXPIRY_SECONDS = 300; // 5 minutes

    private static class OtpEntry {
        final String code;
        final Instant expiresAt;
        OtpEntry(String code, Instant expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }
    }

    // In production, use Redis or a DB with TTL instead
    private final Map<String, OtpEntry> cache = new ConcurrentHashMap<>();

    @Override
    public void generateAndSendOtp(String userId) {
        // 1. generate
        int num = 100000 + RANDOM.nextInt(900000);
        String otpCode = String.valueOf(num);

        // 2. store
        cache.put(userId, new OtpEntry(otpCode, Instant.now().plusSeconds(EXPIRY_SECONDS)));

        // 3. send via SMS/Email
        sendSmsOrEmail(userId, otpCode);
    }

    @Override
    public boolean verifyOtp(String userId, String otp) {
        OtpEntry entry = cache.get(userId);
        if (entry == null || Instant.now().isAfter(entry.expiresAt)) {
            return false;
        }
        boolean matches = entry.code.equals(otp);
        if (matches) {
            cache.remove(userId);
        }
        return matches;
    }

    private void sendSmsOrEmail(String userId, String otp) {
        // integrate Twilio/SendGrid/etc. here
        System.out.printf("Sending OTP %s to user %s%n", otp, userId);

//        email otp template
//        try{
//            emailService.sendOtpEmail(userId,otpMailSubject,otp);
//        } catch (Exception e) {
//            throw new RuntimeException("error in sending OTP mail"+e);
//        }

    }

}
