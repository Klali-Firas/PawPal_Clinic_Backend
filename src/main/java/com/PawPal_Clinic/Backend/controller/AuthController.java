package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.service.JwtUtility;
import com.PawPal_Clinic.Backend.service.UtilisateurService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")  // Replace with your frontend's origin
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private JwtUtility jwtUtility;

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUser(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();

        // Extract user details
        String prenom = user.getAttribute("given_name");
        String nom = user.getAttribute("family_name");
        String email = user.getAttribute("email");
        String picture = user.getAttribute("picture");

        // Prepare response
        Map<String, Object> response = new HashMap<>();

        // Check if email exists
        Optional<UtilisateurDto> existingUser = utilisateurService.getUtilisateurByEmail(email);
        if (existingUser.isEmpty()) {
            // Save user details to the database
            UtilisateurDto utilisateurDto = new UtilisateurDto(null, email, "proprietaire", prenom, nom, null, null);
            try {
                UtilisateurDto createdUser = utilisateurService.createUtilisateur(utilisateurDto);
                response.put("user", createdUser);
            } catch (Exception e) {
                response.put("message", "login failed");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("photo", picture);
            response.put("user", existingUser.get());
        }

        // Generate JWT token
        String token = jwtUtility.generateToken(email, "proprietaire");
        response.put("token", token);

        // Log user details as JSON
        try {
            String userDetailsJson = jacksonObjectMapper.writeValueAsString(user);
            System.out.println(userDetailsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(response);
    }
}