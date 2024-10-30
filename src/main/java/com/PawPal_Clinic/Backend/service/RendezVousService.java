package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.RendezVousDto;
import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.model.RendezVous;
import com.PawPal_Clinic.Backend.model.Utilisateur;
import com.PawPal_Clinic.Backend.repository.RendezVousRepository;
import com.PawPal_Clinic.Backend.repository.AnimauxRepository;
import com.PawPal_Clinic.Backend.repository.UtilisateurRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            sendEmailToVet(veterinaire.getEmail(), updatedRendezVous);

            return Optional.of(convertToDto(updatedRendezVous));
        }
        return Optional.empty();
    }

    private void sendEmailToVet(String vetEmail, RendezVous rendezVous) {
        UtilisateurDto client = utilisateurService.getProprietaireByAnimalId(rendezVous.getAnimal().getId()).get();
        SimpleMailMessage message = new SimpleMailMessage();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        message.setTo(vetEmail);
        message.setSubject("New RendezVous Assigned");
        message.setText("Dear Veterinaire,\n\nYou have been assigned a new RendezVous.\n\nDetails:\n" +
                "RendezVous ID: " + rendezVous.getId() + "\n" +
                "Date & Time: " + LocalDateTime.ofInstant(rendezVous.getDateRendezVous(), ZoneId.systemDefault()).format(formatter) + "\n" +
                "Client: " + client.getNom() +' '+client.getPrenom() + "\n" +
                "Animal: " + rendezVous.getAnimal().getNom() + " " + rendezVous.getAnimal().getRace() + "\n\n" +
                "Please confirm your availability.\n\nThank you.");
        emailSender.send(message);
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
            rendezVous.setVeterinaire(utilisateurRepository.findById(rendezVousDto.getVeterinaireId()).orElseThrow());
        }
        rendezVous.setDateRendezVous(rendezVousDto.getDateRendezVous());
        rendezVous.setStatut(rendezVousDto.getStatut());
        rendezVous.setMotif(rendezVousDto.getMotif());
        rendezVous.setCreeLe(rendezVousDto.getCreeLe());
        return rendezVous;
    }
}