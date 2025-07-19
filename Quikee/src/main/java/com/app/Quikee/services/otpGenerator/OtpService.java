package com.app.Quikee.services.otpGenerator;

public interface OtpService {

   void generateAndSendOtp(String userId);

   boolean verifyOtp(String userId, String otp);
}
