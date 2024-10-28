package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")  // Replace with your frontend's origin
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUser(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();

        // Extract user details
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");
        String picture = user.getAttribute("picture");

        // Check if email exists
        Optional<UtilisateurDto> existingUser = utilisateurService.getUtilisateurByEmail(email);
        if (existingUser.isEmpty()) {
            // Save user details to the database
            UtilisateurDto utilisateurDto = new UtilisateurDto(null, email, "proprietaire", name.split(" ")[0], name.split(" ")[1], null, null);
            utilisateurService.createUtilisateur(utilisateurDto);
        }

        // Prepare response
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", name);
        userDetails.put("email", email);
        userDetails.put("picture", picture);
        return ResponseEntity.ok(userDetails);
    }
}