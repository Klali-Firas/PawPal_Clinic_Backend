package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.RendezVousDto;
import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.model.RendezVous;
import com.PawPal_Clinic.Backend.model.Utilisateur;
import com.PawPal_Clinic.Backend.repository.RendezVousRepository;
import com.PawPal_Clinic.Backend.repository.AnimauxRepository;
import com.PawPal_Clinic.Backend.repository.UtilisateurRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private JavaMailSender emailSender;

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

            return Optional.of(convertToDto(updatedRendezVous));
        }
        return Optional.empty();
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
                rendezVous.getMotif(),
                rendezVous.getCreeLe()
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
        rendezVous.setMotif(rendezVousDto.getMotif());
        rendezVous.setCreeLe(rendezVousDto.getCreeLe());
        return rendezVous;
    }
}