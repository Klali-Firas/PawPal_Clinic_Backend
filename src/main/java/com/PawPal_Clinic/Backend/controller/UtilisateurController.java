package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.UtilisateurDto;
import com.PawPal_Clinic.Backend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<UtilisateurDto>> getAllUtilisateurs() {
        List<UtilisateurDto> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDto> getUtilisateurById(@PathVariable Integer id) {
        Optional<UtilisateurDto> utilisateur = utilisateurService.getUtilisateurById(id);
        return utilisateur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UtilisateurDto> createUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        UtilisateurDto createdUtilisateur = utilisateurService.createUtilisateur(utilisateurDto);
        return ResponseEntity.ok(createdUtilisateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDto> updateUtilisateur(@PathVariable Integer id, @RequestBody UtilisateurDto utilisateurDto) {
        Optional<UtilisateurDto> updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateurDto);
        return updatedUtilisateur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Integer id) {
        boolean deleted = utilisateurService.deleteUtilisateur(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/veterinaire/{id}")
    public ResponseEntity<UtilisateurDto> getVeterinaireById(@PathVariable Integer id) {
        Optional<UtilisateurDto> veterinaire = utilisateurService.getVeterinaireById(id);
        return veterinaire.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/proprietaire/animal/{animalId}")
    public ResponseEntity<UtilisateurDto> getProprietaireByAnimalId(@PathVariable Integer animalId) {
        Optional<UtilisateurDto> proprietaire = utilisateurService.getProprietaireByAnimalId(animalId);
        return proprietaire.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/veterinaires")
    public ResponseEntity<List<UtilisateurDto>> getAllVeterinaires() {
        List<UtilisateurDto> veterinaires = utilisateurService.getAllVeterinaires();
        return ResponseEntity.ok(veterinaires);
    }
}