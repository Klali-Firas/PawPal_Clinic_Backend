package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.ServicesAnimauxDto;
import com.PawPal_Clinic.Backend.model.ServicesAnimaux;
import com.PawPal_Clinic.Backend.repository.ServicesAnimauxRepository;
import com.PawPal_Clinic.Backend.repository.AnimauxRepository;
import com.PawPal_Clinic.Backend.repository.UtilisateurRepository;
import com.PawPal_Clinic.Backend.repository.ServiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicesAnimauxService {

    @Autowired
    private ServicesAnimauxRepository servicesAnimauxRepository;
    @Autowired
    private AnimauxRepository animauxRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ServicesAnimauxDto> getAllServicesAnimaux() {
        return servicesAnimauxRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ServicesAnimauxDto> getServicesAnimauxById(Integer id) {
        return servicesAnimauxRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    public ServicesAnimauxDto createServicesAnimaux(ServicesAnimauxDto servicesAnimauxDto) {
        ServicesAnimaux servicesAnimaux = convertToEntity(servicesAnimauxDto);
        ServicesAnimaux savedServicesAnimaux = servicesAnimauxRepository.save(servicesAnimaux);
        entityManager.refresh(savedServicesAnimaux);
        return convertToDto(savedServicesAnimaux);
    }

    @Transactional
    public Optional<ServicesAnimauxDto> updateServicesAnimaux(Integer id, ServicesAnimauxDto servicesAnimauxDto) {
        if (!servicesAnimauxRepository.existsById(id)) {
            return Optional.empty();
        }
        ServicesAnimaux servicesAnimaux = convertToEntity(servicesAnimauxDto);
        servicesAnimaux.setId(id);
        ServicesAnimaux updatedServicesAnimaux = servicesAnimauxRepository.save(servicesAnimaux);
        return Optional.of(convertToDto(updatedServicesAnimaux));
    }

    @Transactional
    public boolean deleteServicesAnimaux(Integer id) {
        if (servicesAnimauxRepository.existsById(id)) {
            servicesAnimauxRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ServicesAnimauxDto convertToDto(ServicesAnimaux servicesAnimaux) {
        return new ServicesAnimauxDto(
                servicesAnimaux.getId(),
                servicesAnimaux.getAnimal().getId(),
                servicesAnimaux.getVeterinaire().getId(),
                servicesAnimaux.getService().getId(),
                servicesAnimaux.getDateService(),
                servicesAnimaux.getRemarques()
        );
    }

    private ServicesAnimaux convertToEntity(ServicesAnimauxDto servicesAnimauxDto) {
        ServicesAnimaux servicesAnimaux = new ServicesAnimaux();
        servicesAnimaux.setAnimal(animauxRepository.findById(servicesAnimauxDto.getAnimalId()).orElseThrow());
        servicesAnimaux.setVeterinaire(utilisateurRepository.findById(servicesAnimauxDto.getVeterinaireId()).orElseThrow());
        servicesAnimaux.setService(serviceRepository.findById(servicesAnimauxDto.getServiceId()).orElseThrow());
        servicesAnimaux.setDateService(servicesAnimauxDto.getDateService());
        servicesAnimaux.setRemarques(servicesAnimauxDto.getRemarques());
        return servicesAnimaux;
    }
}