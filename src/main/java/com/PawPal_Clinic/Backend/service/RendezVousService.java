package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.AviDto;
import com.PawPal_Clinic.Backend.dto.RendezVousDto;
import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.model.RendezVous;
import com.PawPal_Clinic.Backend.model.Utilisateur;
import com.PawPal_Clinic.Backend.repository.RendezVousRepository;
import com.PawPal_Clinic.Backend.repository.AnimauxRepository;
import com.PawPal_Clinic.Backend.repository.ServiceRepository;
import com.PawPal_Clinic.Backend.repository.UtilisateurRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private AnimauxRepository animauxRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private AviService aviService;

    @Transactional(readOnly = true)
    public List<RendezVousDto> getAllRendezVous() {
        return rendezVousRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RendezVousDto> getRendezVousById(Integer id) {
        return rendezVousRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    public RendezVousDto createRendezVous(RendezVousDto rendezVousDto) {
        RendezVous rendezVous = convertToEntity(rendezVousDto);
        if (rendezVousDto.getVeterinaireId() != null) {
            rendezVous.setVeterinaire(utilisateurRepository.findById(rendezVousDto.getVeterinaireId()).orElseThrow());
        }
        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
        entityManager.refresh(savedRendezVous);
        return convertToDto(savedRendezVous);
    }

    @Transactional
    public Optional<RendezVousDto> updateRendezVous(Integer id, RendezVousDto rendezVousDto) {
        if (!rendezVousRepository.existsById(id)) {
            return Optional.empty();
        }
        RendezVous rendezVous = convertToEntity(rendezVousDto);
        rendezVous.setId(id);
        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);
        return Optional.of(convertToDto(updatedRendezVous));
    }

    @Transactional
    public boolean deleteRendezVous(Integer id) {
        if (rendezVousRepository.existsById(id)) {
            rendezVousRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Transactional
    public Optional<RendezVousDto> assignVeterinaire(Integer rendezVousId, Integer veterinaireId) {
        Optional<RendezVous> rendezVousOpt = rendezVousRepository.findById(rendezVousId);
        if (rendezVousOpt.isPresent()) {
            RendezVous rendezVous = rendezVousOpt.get();
            Utilisateur veterinaire = utilisateurRepository.findById(veterinaireId)
                    .orElseThrow(() -> new IllegalArgumentException("Veterinaire not found"));
            rendezVous.setVeterinaire(veterinaire);
            rendezVous.setStatut("confirme"); // Set statut to "confirme"
            RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);

            // Send email to the vet
            sendEmailToVet(veterinaire, updatedRendezVous);

            // Add event to Google Calendar
            try {
                UtilisateurDto client = utilisateurService.getProprietaireByAnimalId(rendezVous.getAnimal().getId()).get();
                googleCalendarService.addEventToCalendar(
                        client.getEmail(),
                        "Rendez-vous with " + veterinaire.getNom() + " " + veterinaire.getPrenom(),
                        "Appointment for " + rendezVous.getAnimal().getNom(),
                        Date.from(rendezVous.getDateRendezVous()),
                        Date.from(rendezVous.getDateRendezVous().plusSeconds(3600)), // Assuming 1-hour duration
                        client.getEmail()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Optional.of(convertToDto(updatedRendezVous));
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<RendezVousDto> getRendezVousByVeterinaireId(Integer veterinaireId) {
        return rendezVousRepository.findByVeterinaireId(veterinaireId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private void sendEmailToVet(Utilisateur vet, RendezVous rendezVous)  {
        UtilisateurDto client = utilisateurService.getProprietaireByAnimalId(rendezVous.getAnimal().getId()).get();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd, MM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {


        String template = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/email-template.html")));

        String emailContent = template
                .replace("{{vetName}}", vet.getNom() + " " + vet.getPrenom())
                .replace("{{appointmentDate}}", LocalDateTime.ofInstant(rendezVous.getDateRendezVous(), ZoneId.systemDefault()).format(dateFormatter))
                .replace("{{appointmentTime}}", LocalDateTime.ofInstant(rendezVous.getDateRendezVous(), ZoneId.systemDefault()).format(timeFormatter))
                .replace("{{clientName}}", client.getNom() + " " + client.getPrenom())
                .replace("{{animalName}}", rendezVous.getAnimal().getNom())
                .replace("{{animalRace}}", rendezVous.getAnimal().getRace());

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(vet.getEmail());
        helper.setSubject("Nouvelle Notification de Rendez-vous");
        helper.setText(emailContent, true); // true indicates HTML content
        emailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RendezVousDto convertToDto(RendezVous rendezVous) {
        Integer veterinaireId = rendezVous.getVeterinaire() != null ? rendezVous.getVeterinaire().getId() : null;
        return new RendezVousDto(
                rendezVous.getId(),
                rendezVous.getAnimal().getId(),
                veterinaireId,
                rendezVous.getDateRendezVous(),
                rendezVous.getStatut(),
                rendezVous.getMotif().getId(),
                rendezVous.getCreeLe(),
                rendezVous.getRemarques()
        );
    }

    private RendezVous convertToEntity(RendezVousDto rendezVousDto) {
        RendezVous rendezVous = new RendezVous();
        rendezVous.setAnimal(animauxRepository.findById(rendezVousDto.getAnimalId()).orElseThrow());
        if (rendezVousDto.getVeterinaireId() != null) {
            System.out.println("called");
            rendezVous.setVeterinaire(utilisateurRepository.findById(rendezVousDto.getVeterinaireId()).orElseThrow());
        }else {
            rendezVous.setVeterinaire(null);
        }
        rendezVous.setDateRendezVous(Instant.parse(rendezVousDto.getDateRendezVous().toString()));
        rendezVous.setStatut(rendezVousDto.getStatut());
        rendezVous.setMotif(serviceRepository.findById(rendezVousDto.getMotif()).get());
        rendezVous.setCreeLe(rendezVousDto.getCreeLe());
        rendezVous.setRemarques(rendezVousDto.getRemarques());
        return rendezVous;
    }


    public ByteArrayInputStream exportRendezVousToCsv() {
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

        // Write BOM
        writer.write('\uFEFF');

        // Write CSV header
        writer.println("Rendez Vous ID,Animal ID,Animal Name,Animal Race,Proprietaire ID,Proprietaire Name,Veterinaire ID,Veterinaire Name,Date RendezVous,Statut,Motif,Cree Le,Remarques,Avis Note,Avis Comment");

        // Write CSV data
        for (RendezVous rendezVous : rendezVousList) {
            String animalId = rendezVous.getAnimal().getId().toString();
            String animalName = rendezVous.getAnimal().getNom() != null ? rendezVous.getAnimal().getNom() : "none";
            String animalRace = rendezVous.getAnimal().getRace() != null ? rendezVous.getAnimal().getRace() : "none";
            String proprietaireId = rendezVous.getAnimal().getProprietaire().getId().toString();
            String proprietaireName = rendezVous.getAnimal().getProprietaire().getNom() + " " + rendezVous.getAnimal().getProprietaire().getPrenom();
            String veterinaireId = rendezVous.getVeterinaire() != null ? rendezVous.getVeterinaire().getId().toString() : "none";
            String veterinaireName = rendezVous.getVeterinaire() != null ? rendezVous.getVeterinaire().getNom() + " " + rendezVous.getVeterinaire().getPrenom() : "none";
            String avisNote = "none";
            String avisComment = "none";

            Optional<AviDto> avi = aviService.getAviByRendezVousIdAndProprietaireId(rendezVous.getId(), rendezVous.getAnimal().getProprietaire().getId());
            if (avi.isPresent()) {
                avisNote = avi.get().getNote().toString();
                avisComment = avi.get().getCommentaire();
            }

            writer.println(String.join(",",
                    rendezVous.getId().toString(),
                    animalId,
                    animalName,
                    animalRace,
                    proprietaireId,
                    proprietaireName,
                    veterinaireId,
                    veterinaireName,
                    rendezVous.getDateRendezVous().toString(),
                    rendezVous.getStatut(),
                    rendezVous.getMotif().getNomService(),
                    rendezVous.getCreeLe().toString(),
                    rendezVous.getRemarques() != null ? rendezVous.getRemarques() : "none",
                    avisNote,
                    avisComment
            ));
        }

        writer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }
}