package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.ServiceDto;
import com.PawPal_Clinic.Backend.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        List<ServiceDto> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Integer id) {
        Optional<ServiceDto> service = serviceService.getServiceById(id);
        return service.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto serviceDto) {
        ServiceDto createdService = serviceService.createService(serviceDto);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Integer id, @RequestBody ServiceDto serviceDto) {
        Optional<ServiceDto> updatedService = serviceService.updateService(id, serviceDto);
        return updatedService.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        boolean deleted = serviceService.deleteService(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}