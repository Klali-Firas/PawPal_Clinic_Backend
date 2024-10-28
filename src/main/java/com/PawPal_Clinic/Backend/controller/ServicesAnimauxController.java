package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.ServicesAnimauxDto;
import com.PawPal_Clinic.Backend.service.ServicesAnimauxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/services-animaux")
public class ServicesAnimauxController {

    @Autowired
    private ServicesAnimauxService servicesAnimauxService;

    @GetMapping
    public ResponseEntity<List<ServicesAnimauxDto>> getAllServicesAnimaux() {
        List<ServicesAnimauxDto> servicesAnimaux = servicesAnimauxService.getAllServicesAnimaux();
        return ResponseEntity.ok(servicesAnimaux);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicesAnimauxDto> getServicesAnimauxById(@PathVariable Integer id) {
        Optional<ServicesAnimauxDto> servicesAnimaux = servicesAnimauxService.getServicesAnimauxById(id);
        return servicesAnimaux.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServicesAnimauxDto> createServicesAnimaux(@RequestBody ServicesAnimauxDto servicesAnimauxDto) {
        ServicesAnimauxDto createdServicesAnimaux = servicesAnimauxService.createServicesAnimaux(servicesAnimauxDto);
        return ResponseEntity.ok(createdServicesAnimaux);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicesAnimauxDto> updateServicesAnimaux(@PathVariable Integer id, @RequestBody ServicesAnimauxDto servicesAnimauxDto) {
        Optional<ServicesAnimauxDto> updatedServicesAnimaux = servicesAnimauxService.updateServicesAnimaux(id, servicesAnimauxDto);
        return updatedServicesAnimaux.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicesAnimaux(@PathVariable Integer id) {
        boolean deleted = servicesAnimauxService.deleteServicesAnimaux(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}