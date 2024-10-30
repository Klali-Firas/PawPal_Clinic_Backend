package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.model.Animaux;
import com.PawPal_Clinic.Backend.model.Utilisateur;
import com.PawPal_Clinic.Backend.repository.AnimauxRepository;
import com.PawPal_Clinic.Backend.repository.UtilisateurRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AnimauxRepository animauxRepository;

    @Transactional(readOnly = true)
    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UtilisateurDto> getUtilisateurById(Integer id) {
        return utilisateurRepository.findById(id).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UtilisateurDto> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email).map(this::convertToDto);
    }

    @Transactional
    public UtilisateurDto createUtilisateur(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = convertToEntity(utilisateurDto);
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        entityManager.refresh(savedUtilisateur);
        return convertToDto(savedUtilisateur);
    }

    @Transactional
    public Optional<UtilisateurDto> updateUtilisateur(Integer id, UtilisateurDto utilisateurDto) {
        if (!utilisateurRepository.existsById(id)) {
            return Optional.empty();
        }
        Utilisateur utilisateur = convertToEntity(utilisateurDto);
        utilisateur.setId(id);
        Utilisateur updatedUtilisateur = utilisateurRepository.save(utilisateur);
        return Optional.of(convertToDto(updatedUtilisateur));
    }

    @Transactional
    public boolean deleteUtilisateur(Integer id) {
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Optional<UtilisateurDto> getVeterinaireById(Integer id) {
        return utilisateurRepository.findById(id)
                .filter(utilisateur -> "veterinaire".equals(utilisateur.getRole()))
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UtilisateurDto> getProprietaireByAnimalId(Integer animalId) {
        Optional<Animaux> animal = animauxRepository.findById(animalId);
        if (animal.isPresent()) {
            Utilisateur proprietaire = animal.get().getProprietaire();
            if (proprietaire != null) {
                return Optional.of(convertToDto(proprietaire));
            }
        }
        return Optional.empty();
    }


    @Transactional(readOnly = true)
    public List<UtilisateurDto> getAllVeterinaires() {
        return utilisateurRepository.findAll().stream()
                .filter(utilisateur -> "veterinaire".equals(utilisateur.getRole()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private UtilisateurDto convertToDto(Utilisateur utilisateur) {
        return new UtilisateurDto(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getPrenom(),
                utilisateur.getNom(),
                utilisateur.getTelephone(),
                utilisateur.getCreeLe()
        );
    }

    private Utilisateur convertToEntity(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setRole(utilisateurDto.getRole());
        utilisateur.setPrenom(utilisateurDto.getPrenom());
        utilisateur.setNom(utilisateurDto.getNom());
        utilisateur.setTelephone(utilisateurDto.getTelephone());
        utilisateur.setCreeLe(utilisateurDto.getCreeLe());
        return utilisateur;
    }
}