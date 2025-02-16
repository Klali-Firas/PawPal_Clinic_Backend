package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.service.JwtUtility;
import com.PawPal_Clinic.Backend.service.UtilisateurService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "${front.url}")  // Replace with your frontend's origin
public class AuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
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

    @PostMapping("/verifyGoogleIdToken")
    public ResponseEntity<Map<String, Object>> verifyGoogleIdToken(@RequestBody Map<String, String> requestBody) {
        String idTokenString = requestBody.get("idTokenString");
        System.out.println("hello id verifier");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId)) // Replace with your client ID
                .build();
        System.out.println(idTokenString);
        System.out.println(verifier.getAudience());
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            System.out.println(idToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String prenom = (String) payload.get("given_name");
                String nom = (String) payload.get("family_name");
                String picture = (String) payload.get("picture");

                Map<String, Object> response = new HashMap<>();

                Optional<UtilisateurDto> existingUser = utilisateurService.getUtilisateurByEmail(email);
                if (existingUser.isEmpty()) {
                    // Create and save new user
                    UtilisateurDto newUser = new UtilisateurDto(null, email, "proprietaire", prenom, nom, null, null, getRefreshTokenFromRequestOrService());
                    utilisateurService.createUtilisateur(newUser);
                    response.put("user", newUser);
                } else {
                    UtilisateurDto utilisateurDto = existingUser.get();
                    // Check if the user has a refresh token
                    if (utilisateurDto.getRefreshToken() == null || utilisateurDto.getRefreshToken().isEmpty()) {
                        // Assuming you have a way to get the refresh token, e.g., from the request or another service
                        String refreshToken = getRefreshTokenFromRequestOrService();
                        utilisateurDto.setRefreshToken(refreshToken);
                        utilisateurService.updateUtilisateur(utilisateurDto.getId(), utilisateurDto);
                    }
                    response.put("user", utilisateurDto);
                }

                String token = jwtUtility.generateToken(email, "proprietaire");
                response.put("token", token);
                response.put("photo", picture);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Invalid ID token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Internal server error"));
        }
    }

    // Placeholder method to get the refresh token
    private String getRefreshTokenFromRequestOrService() {
        // Implement your logic to retrieve the refresh token
        return "sample_refresh_token";
    }
}