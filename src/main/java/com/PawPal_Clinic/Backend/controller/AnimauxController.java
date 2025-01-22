package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.AnimauxDto;
import com.PawPal_Clinic.Backend.service.AnimauxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/public/animaux")
public class AnimauxController {

    @Autowired
    private AnimauxService animauxService;

    @GetMapping
    public List<AnimauxDto> getAllAnimaux() {
        return animauxService.getAllAnimaux();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimauxDto> getAnimauxById(@PathVariable Integer id) {
        Optional<AnimauxDto> animaux = animauxService.getAnimauxById(id);
        return animaux.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public AnimauxDto createAnimaux(@RequestBody AnimauxDto AnimauxDto) {
        System.out.println("called");
        return animauxService.createAnimaux(AnimauxDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimauxDto> updateAnimaux(@PathVariable Integer id, @RequestBody AnimauxDto AnimauxDto) {
        Optional<AnimauxDto> updatedAnimaux = animauxService.updateAnimaux(id, AnimauxDto);
        return updatedAnimaux.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimaux(@PathVariable Integer id) {
        if (animauxService.deleteAnimaux(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/proprietaire/{proprietaireId}")
    public ResponseEntity<List<AnimauxDto>> getAnimauxByProprietaireId(@PathVariable Integer proprietaireId) {
        List<AnimauxDto> animaux = animauxService.getAnimauxByProprietaireId(proprietaireId);
        if (animaux.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(animaux);
    }
}
