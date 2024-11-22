package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.RendezVousDto;
import com.PawPal_Clinic.Backend.service.EmailService;
import com.PawPal_Clinic.Backend.service.RendezVousService;

import io.jsonwebtoken.io.IOException;
import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("api/invite")
    public ResponseEntity<String> sendRendezVousInvite(@RequestBody RendezVousDto rendezVousDto, 
                                                       @RequestParam String email) throws java.io.IOException {
        try {
            emailService.sendRendezVousInvite(rendezVousDto, email);
            return ResponseEntity.ok("Invitation sent successfully.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send invitation: " + e.getMessage());
        }
    }
}
