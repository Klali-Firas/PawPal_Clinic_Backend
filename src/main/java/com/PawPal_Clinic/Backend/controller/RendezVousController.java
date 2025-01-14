package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.RendezVousDto;
import com.PawPal_Clinic.Backend.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @GetMapping
    public ResponseEntity<List<RendezVousDto>> getAllRendezVous() {
        List<RendezVousDto> rendezVous = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVousDto> getRendezVousById(@PathVariable Integer id) {
        Optional<RendezVousDto> rendezVous = rendezVousService.getRendezVousById(id);
        return rendezVous.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RendezVousDto> createRendezVous(@RequestBody RendezVousDto rendezVousDto) {
        RendezVousDto createdRendezVous = rendezVousService.createRendezVous(rendezVousDto);
        return ResponseEntity.ok(createdRendezVous);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RendezVousDto> updateRendezVous(@PathVariable Integer id, @RequestBody RendezVousDto rendezVousDto) {
        Optional<RendezVousDto> updatedRendezVous = rendezVousService.updateRendezVous(id, rendezVousDto);
        return updatedRendezVous.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRendezVous(@PathVariable Integer id) {
        boolean deleted = rendezVousService.deleteRendezVous(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{rendezVousId}/assign-veterinaire/{veterinaireId}")
    public ResponseEntity<RendezVousDto> assignVeterinaire(@PathVariable Integer rendezVousId, @PathVariable Integer veterinaireId) {
        Optional<RendezVousDto> updatedRendezVous = rendezVousService.assignVeterinaire(rendezVousId, veterinaireId);
        return updatedRendezVous.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/veterinaire/{veterinaireId}")
    public ResponseEntity<List<RendezVousDto>> getRendezVousByVeterinaireId(@PathVariable Integer veterinaireId) {
        List<RendezVousDto> rendezVous = rendezVousService.getRendezVousByVeterinaireId(veterinaireId);
        if (rendezVous.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<InputStreamResource> exportRendezVousToCsv() {
        ByteArrayInputStream byteArrayInputStream = rendezVousService.exportRendezVousToCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=rendezvous_report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(byteArrayInputStream));
    }
}