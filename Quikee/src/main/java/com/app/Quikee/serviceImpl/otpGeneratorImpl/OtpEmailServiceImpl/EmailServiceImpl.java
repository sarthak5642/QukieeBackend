package com.app.Quikee.serviceImpl.otpGeneratorImpl.OtpEmailServiceImpl;

import com.app.Quikee.services.otpGenerator.OtpEmailService.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String subject, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(getHtmlTemplate(otp), true);
        mailSender.send(message);
    }

    private String getHtmlTemplate(String otp) {
        StringBuilder digitsHtml = new StringBuilder();
        for (char ch : otp.toCharArray()) {
            digitsHtml.append("<div class=\"otp-digit\">")
                    .append(ch)
                    .append("</div>");
        }

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Qukiee - OTP</title>
                <!-- Include styles here (as from your final template) -->
                </head>
                <body>
                <div class="email-container">
                  <div class="header">
                    <div class="logo">Qukiee</div>
                    <div class="tagline">Colorful & Energetic • Luxury & Elegant</div>
                  </div>
                  <div class="content">
                    <div class="wardrobe-tag">Confirm Access</div>
                    <div class="otp-message">Use this OTP to verify your login</div>
                    <div class="otp-box">
                                            %s
                     </div>
            <div class="info-bubble">
              This OTP is valid for 10 minutes. Do not share it with anyone, not even with Qukiee staff.
            </div>
          </div>
          <div class="footer">
            Thank you for choosing Qukiee!<br/>
            <div class="social-icons">
              <a href="#">F</a><a href="#">T</a><a href="#">I</a>
            </div>
          </div>
        </div>
        </body>
        </html>
        """.formatted(digitsHtml.toString());
    }

}

