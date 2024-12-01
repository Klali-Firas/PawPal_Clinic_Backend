package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.AviDto;
import com.PawPal_Clinic.Backend.model.Avi;
import com.PawPal_Clinic.Backend.repository.AviRepository;
import com.PawPal_Clinic.Backend.repository.RendezVousRepository;
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
public class AviService {

    @Autowired
    private AviRepository aviRepository;
    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<AviDto> getAllAvis() {
        return aviRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AviDto> getAviById(Integer id) {
        return aviRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<AviDto> getAvisByRendezVousId(Integer rendezVousId) {
        return aviRepository.findByRendezVousId(rendezVousId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Optional<AviDto> getAviByRendezVousIdAndProprietaireId(Integer rendezVousId, Integer proprietaireId) {
        return aviRepository.findByRendezVousIdAndProprietaireId(rendezVousId, proprietaireId)
                .map(this::convertToDto);
    }

    @Transactional
    public AviDto createAvi(AviDto aviDto) {
        Avi avi = convertToEntity(aviDto);
        Avi savedAvi = aviRepository.save(avi);
        entityManager.refresh(savedAvi);
        return convertToDto(savedAvi);
    }

    @Transactional
    public Optional<AviDto> updateAvi(Integer id, AviDto aviDto) {
        if (!aviRepository.existsById(id)) {
            return Optional.empty();
        }
        Avi avi = convertToEntity(aviDto);
        avi.setId(id);
        Avi updatedAvi = aviRepository.save(avi);
        return Optional.of(convertToDto(updatedAvi));
    }

    @Transactional
    public boolean deleteAvi(Integer id) {
        if (aviRepository.existsById(id)) {
            aviRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AviDto convertToDto(Avi avi) {
        return new AviDto(avi.getId(), avi.getRendezVous().getId(), avi.getProprietaire().getId(), avi.getNote(), avi.getCommentaire(), avi.getCreeLe());
    }

    private Avi convertToEntity(AviDto aviDto) {
        Avi avi = new Avi();
        avi.setRendezVous(rendezVousRepository.findById(aviDto.getRendezVousId()).orElse(null));
        avi.setProprietaire(utilisateurRepository.findById(aviDto.getProprietaireId()).orElse(null));
        avi.setNote(aviDto.getNote());
        avi.setCommentaire(aviDto.getCommentaire());
        avi.setCreeLe(aviDto.getCreeLe());
        return avi;
    }
}