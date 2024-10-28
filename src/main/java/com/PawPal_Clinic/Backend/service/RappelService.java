package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.RappelDto;
import com.PawPal_Clinic.Backend.model.Rappel;
import com.PawPal_Clinic.Backend.repository.RappelRepository;
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
public class RappelService {

    @Autowired
    private RappelRepository rappelRepository;
    @Autowired
    private AnimauxRepository animauxRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<RappelDto> getAllRappels() {
        return rappelRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RappelDto> getRappelById(Integer id) {
        return rappelRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    public RappelDto createRappel(RappelDto rappelDto) {
        Rappel rappel = convertToEntity(rappelDto);
        Rappel savedRappel = rappelRepository.save(rappel);
        entityManager.refresh(savedRappel);
        return convertToDto(savedRappel);
    }

    @Transactional
    public Optional<RappelDto> updateRappel(Integer id, RappelDto rappelDto) {
        if (!rappelRepository.existsById(id)) {
            return Optional.empty();
        }
        Rappel rappel = convertToEntity(rappelDto);
        rappel.setId(id);
        Rappel updatedRappel = rappelRepository.save(rappel);
        return Optional.of(convertToDto(updatedRappel));
    }

    @Transactional
    public boolean deleteRappel(Integer id) {
        if (rappelRepository.existsById(id)) {
            rappelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private RappelDto convertToDto(Rappel rappel) {
        return new RappelDto(rappel.getId(), rappel.getAnimal().getId(), rappel.getVeterinaire().getId(), rappel.getDateRappel(), rappel.getTypeRappel(), rappel.getStatut(), rappel.getCreeLe());
    }

    private Rappel convertToEntity(RappelDto rappelDto) {
        Rappel rappel = new Rappel();
        rappel.setAnimal(animauxRepository.findById(rappelDto.getAnimalId()).orElseThrow());
        rappel.setVeterinaire(utilisateurRepository.findById(rappelDto.getVeterinaireId()).orElseThrow());
        rappel.setDateRappel(rappelDto.getDateRappel());
        rappel.setTypeRappel(rappelDto.getTypeRappel());
        rappel.setStatut(rappelDto.getStatut());
        rappel.setCreeLe(rappelDto.getCreeLe());
        return rappel;
    }
}