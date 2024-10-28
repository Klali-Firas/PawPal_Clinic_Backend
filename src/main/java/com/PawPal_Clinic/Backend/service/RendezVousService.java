package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.RendezVousDto;
import com.PawPal_Clinic.Backend.model.RendezVous;
import com.PawPal_Clinic.Backend.repository.RendezVousRepository;
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
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private AnimauxRepository animauxRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    private RendezVousDto convertToDto(RendezVous rendezVous) {
        return new RendezVousDto(rendezVous.getId(), rendezVous.getAnimal().getId(), rendezVous.getVeterinaire().getId(), rendezVous.getDateRendezVous(), rendezVous.getStatut(), rendezVous.getMotif(), rendezVous.getCreeLe());
    }

    private RendezVous convertToEntity(RendezVousDto rendezVousDto) {
        RendezVous rendezVous = new RendezVous();
        rendezVous.setAnimal(animauxRepository.findById(rendezVousDto.getAnimalId()).orElseThrow());
        rendezVous.setVeterinaire(utilisateurRepository.findById(rendezVousDto.getVeterinaireId()).orElseThrow());
        rendezVous.setDateRendezVous(rendezVousDto.getDateRendezVous());
        rendezVous.setStatut(rendezVousDto.getStatut());
        rendezVous.setMotif(rendezVousDto.getMotif());
        rendezVous.setCreeLe(rendezVousDto.getCreeLe());
        return rendezVous;
    }
}