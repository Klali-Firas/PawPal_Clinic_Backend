package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.AviDto;
import com.PawPal_Clinic.Backend.service.AviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/avis")
public class AviController {

    @Autowired
    private AviService aviService;

    @GetMapping
    public ResponseEntity<List<AviDto>> getAllAvis() {
        List<AviDto> avis = aviService.getAllAvis();
        return ResponseEntity.ok(avis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AviDto> getAviById(@PathVariable Integer id) {
        Optional<AviDto> avi = aviService.getAviById(id);
        return avi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/rendezvous/{rendezVousId}")
    public ResponseEntity<List<AviDto>> getAvisByRendezVousId(@PathVariable Integer rendezVousId) {
        List<AviDto> avis = aviService.getAvisByRendezVousId(rendezVousId);
        if (avis.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avis);
    }

    @PostMapping
    public ResponseEntity<AviDto> createAvi(@RequestBody AviDto aviDto) {
        AviDto createdAvi = aviService.createAvi(aviDto);
        return ResponseEntity.ok(createdAvi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AviDto> updateAvi(@PathVariable Integer id, @RequestBody AviDto aviDto) {
        Optional<AviDto> updatedAvi = aviService.updateAvi(id, aviDto);
        return updatedAvi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvi(@PathVariable Integer id) {
        boolean deleted = aviService.deleteAvi(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/rendezvous/{rendezVousId}/proprietaire/{proprietaireId}")
    public ResponseEntity<AviDto> getAviByRendezVousIdAndProprietaireId(@PathVariable Integer rendezVousId, @PathVariable Integer proprietaireId) {
        Optional<AviDto> avi = aviService.getAviByRendezVousIdAndProprietaireId(rendezVousId, proprietaireId);
        return avi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}