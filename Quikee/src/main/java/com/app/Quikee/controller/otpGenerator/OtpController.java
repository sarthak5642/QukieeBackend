package com.app.Quikee.controller.otpGenerator;

import com.app.Quikee.DTO.OtpRequest;
import com.app.Quikee.services.otpGenerator.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService){
            this.otpService = otpService;
    }

    @GetMapping(path = "/generateOtp")
    public ResponseEntity<Void> generateOtp(@RequestParam String userId){
        otpService.generateAndSendOtp(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<Void> verifyOtp(@RequestBody OtpRequest request) {
        boolean valid = otpService.verifyOtp(request.getUserId(), request.getOtp());
        if (valid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(401).build();
        }
    }

}
