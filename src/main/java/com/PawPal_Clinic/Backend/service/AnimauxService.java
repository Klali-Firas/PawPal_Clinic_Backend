package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.model.Animaux;
import com.PawPal_Clinic.Backend.repository.AnimauxRepository;
import com.PawPal_Clinic.Backend.dto.AnimauxDto;
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
public class AnimauxService {

    @Autowired
    private AnimauxRepository animauxRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(readOnly = true)
    public List<AnimauxDto> getAllAnimaux() {
        return animauxRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AnimauxDto> getAnimauxById(Integer id) {
        return animauxRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public AnimauxDto createAnimaux(AnimauxDto animauxDto) {
        System.out.println("called");
        Animaux animaux = convertToEntity(animauxDto);
        Animaux savedAnimaux = animauxRepository.save(animaux);
        // Refresh the entity to get the updated value from the database
        entityManager.refresh(savedAnimaux);
        return convertToDTO(savedAnimaux);
    }


    @Transactional
    public Optional<AnimauxDto> updateAnimaux(Integer id, AnimauxDto AnimauxDto) {
        if (!animauxRepository.existsById(id)) {
            return Optional.empty();
        }
        Animaux animaux = convertToEntity(AnimauxDto);
        animaux.setId(id);
        Animaux updatedAnimaux = animauxRepository.save(animaux);
        return Optional.of(convertToDTO(updatedAnimaux));
    }

    @Transactional
    public boolean deleteAnimaux(Integer id) {
        if (animauxRepository.existsById(id)) {
            animauxRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AnimauxDto convertToDTO(Animaux animaux) {
        AnimauxDto dto = new AnimauxDto(animaux.getId(),animaux.getProprietaire().getId(),animaux.getNom(), animaux.getRace(), animaux.getAge(), animaux.getHistoriqueMedical(), animaux.getCreeLe());

        return dto;
    }

    private Animaux convertToEntity(AnimauxDto dto) {
        Animaux animaux = new Animaux();
        animaux.setNom(dto.getNom());
        animaux.setRace(dto.getRace());
        animaux.setAge(dto.getAge());
        animaux.setHistoriqueMedical(dto.getHistoriqueMedical());
        animaux.setProprietaire(utilisateurRepository.findById(dto.getProprietaireId()).get());
        // Set proprietaire using repository, etc.
        return animaux;
    }
}
