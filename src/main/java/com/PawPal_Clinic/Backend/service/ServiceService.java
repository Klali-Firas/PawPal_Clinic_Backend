package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.ServiceDto;
import com.PawPal_Clinic.Backend.model.Service;
import com.PawPal_Clinic.Backend.repository.ServiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ServiceDto> getServiceById(Integer id) {
        return serviceRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    public ServiceDto createService(ServiceDto serviceDto) {
        Service service = convertToEntity(serviceDto);
        Service savedService = serviceRepository.save(service);
        entityManager.refresh(savedService);
        return convertToDto(savedService);
    }

    @Transactional
    public Optional<ServiceDto> updateService(Integer id, ServiceDto serviceDto) {
        if (!serviceRepository.existsById(id)) {
            return Optional.empty();
        }
        Service service = convertToEntity(serviceDto);
        service.setId(id);
        Service updatedService = serviceRepository.save(service);
        return Optional.of(convertToDto(updatedService));
    }

    @Transactional
    public boolean deleteService(Integer id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ServiceDto convertToDto(Service service) {
        return new ServiceDto(service.getId(), service.getNomService(), service.getDescription(), service.getPrix());
    }

    private Service convertToEntity(ServiceDto serviceDto) {
        Service service = new Service();
        service.setNomService(serviceDto.getNomService());
        service.setDescription(serviceDto.getDescription());
        service.setPrix(serviceDto.getPrix());
        return service;
    }
}