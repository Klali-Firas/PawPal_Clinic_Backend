package com.PawPal_Clinic.Backend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource, ClientRegistrationRepository clientRegistrationRepository) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

   
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))  // Apply CORS configurations
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
                        .requestMatchers("/", "/api/public/**").permitAll() // Allow public access to the /api/public/** endpoints
                        .anyRequest().authenticated()
                )
               .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestResolver(authorizationRequestResolver(clientRegistrationRepository))
                        )
                        .defaultSuccessUrl("http://localhost:4200/home", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().flush();
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/logout", "/api/**") // Disable CSRF for the logout endpoint and /api/** paths
                );
    
        return http.build();
    }
    @Bean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        resolver.setAuthorizationRequestCustomizer(customizer -> customizer.additionalParameters(params -> {
            // Check if the user has previously logged in
            boolean userPreviouslyLoggedIn = checkUserPreviouslyLoggedIn();
            if (!userPreviouslyLoggedIn) {
                params.put("prompt", "consent");
            }
        }));
        return resolver;
    }

    private boolean checkUserPreviouslyLoggedIn() {
        // Implement your logic to check if the user has previously logged in
        // This could involve checking a database or session
        return false; // Placeholder implementation
    }
}
