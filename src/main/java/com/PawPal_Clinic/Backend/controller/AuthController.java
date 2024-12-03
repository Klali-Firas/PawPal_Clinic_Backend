package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.service.JwtUtility;
import com.PawPal_Clinic.Backend.service.UtilisateurService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "${front.url}")  // Replace with your frontend's origin
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private JwtUtility jwtUtility;

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUser(
            @AuthenticationPrincipal OAuth2User user,
            @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
            @RequestParam(required = false) String accountType // Optional parameter to get account type
    ) {

        // Extract user details from Google
        String email = user.getAttribute("email");
        String prenom = user.getAttribute("given_name");
        String nom = user.getAttribute("family_name");
        String picture = user.getAttribute("picture");

        // Prepare response map
        Map<String, Object> response = new HashMap<>();

        // Check if the user already exists
        Optional<UtilisateurDto> existingUser = utilisateurService.getUtilisateurByEmail(email);
        if (existingUser.isEmpty()) {
            // If account type is provided, set it; otherwise, use a default value or prompt for it later
            if (accountType == null || (!accountType.equals("proprietaire") && !accountType.equals("veterinaire"))) {
                response.put("message", "Account type is required or invalid");
                return ResponseEntity.status(400).body(response);
            }

            // Create and save new user with the specified account type
            String refreshToken = authorizedClient.getRefreshToken().getTokenValue();

            UtilisateurDto utilisateurDto = new UtilisateurDto(null, email, accountType, prenom, nom, null, null, refreshToken);
            utilisateurService.createUtilisateur(utilisateurDto);
            response.put("user", utilisateurDto);
        } else {
            UtilisateurDto utilisateurDto = existingUser.get();
            // Check if the user has a refresh token
            if (utilisateurDto.getRefreshToken() == null || utilisateurDto.getRefreshToken().isEmpty()) {
                String refreshToken = authorizedClient.getRefreshToken().getTokenValue();

                // Update the user with the new refresh token
                utilisateurDto.setRefreshToken(refreshToken);
                utilisateurService.updateUtilisateur(utilisateurDto.getId(), utilisateurDto);
            }
            response.put("user", utilisateurDto);
        }

        // Generate and include JWT token
        String token = jwtUtility.generateToken(email, accountType != null ? accountType : "proprietaire");
        response.put("token", token);
        response.put("photo", picture);

        try {
            // Log user details
            System.out.println(user);
            System.out.println(authorizedClient.getRefreshToken().getTokenValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(response);
    }
}