package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "PawPal Clinic";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private UtilisateurService utilisateurService;

    private Credential getCredentials(String email) throws Exception {
        String refreshToken = utilisateurService.getUtilisateurByEmail(email)
                .map(UtilisateurDto::getRefreshToken)
                .orElseThrow(() -> new Exception("Refresh token not found for user: " + email));

        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(clientId, clientSecret)
                .setJsonFactory(JSON_FACTORY)
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .addRefreshListener(new CredentialRefreshListener() {
                    @Override
                    public void onTokenResponse(Credential credential, TokenResponse tokenResponse) {
                        if (tokenResponse.getRefreshToken() != null) {
                            utilisateurService.saveRefreshToken(email, tokenResponse.getRefreshToken());
                        }
                    }

                    @Override
                    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) {
                        // Handle token error response
                    }
                })
                .build()
                .setRefreshToken(refreshToken);

        credential.refreshToken();
        return credential;
    }

    public void addEventToCalendar(String email, String summary, String description, Date startDate, Date endDate, String calendarId) throws Exception {
        listCalendars(email);
        Credential credential = getCredentials(email);

        Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startDate))
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endDate))
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        service.events().insert(calendarId, event).execute();
    }
    public void listCalendars(String email) throws Exception {
        Credential credential = getCredentials(email);
        Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Calendar.CalendarList.List request = service.calendarList().list();
        com.google.api.services.calendar.model.CalendarList calendarList = request.execute();

        for (com.google.api.services.calendar.model.CalendarListEntry calendarListEntry : calendarList.getItems()) {
            System.out.println("ID: " + calendarListEntry.getId() + " Summary: " + calendarListEntry.getSummary());
        }
    }

}
