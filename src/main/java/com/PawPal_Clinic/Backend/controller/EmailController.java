package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("api/public/send-email")
    public String sendEmail(@RequestBody Map<String, String> emailRequest) {
        String to = emailRequest.get("to");
        String subject = emailRequest.get("subject");
        String text = emailRequest.get("text");
        emailService.sendSimpleEmail(to, subject, text);
        return "Email sent successfully!";
    }
}
