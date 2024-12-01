package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.CommandeProduitDto;
import com.PawPal_Clinic.Backend.service.CommandeProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/commande-produits")
public class CommandeProduitController {

    @Autowired
    private CommandeProduitService commandeProduitService;

    @GetMapping
    public ResponseEntity<List<CommandeProduitDto>> getAllCommandeProduits() {
        List<CommandeProduitDto> commandeProduits = commandeProduitService.getAllCommandeProduits();
        return ResponseEntity.ok(commandeProduits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeProduitDto> getCommandeProduitById(@PathVariable Integer id) {
        Optional<CommandeProduitDto> commandeProduit = commandeProduitService.getCommandeProduitById(id);
        return commandeProduit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CommandeProduitDto> createCommandeProduit(@RequestBody CommandeProduitDto commandeProduitDto) {
        CommandeProduitDto createdCommandeProduit = commandeProduitService.createCommandeProduit(commandeProduitDto);
        return ResponseEntity.ok(createdCommandeProduit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeProduitDto> updateCommandeProduit(@PathVariable Integer id, @RequestBody CommandeProduitDto commandeProduitDto) {
        Optional<CommandeProduitDto> updatedCommandeProduit = commandeProduitService.updateCommandeProduit(id, commandeProduitDto);
        return updatedCommandeProduit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommandeProduit(@PathVariable Integer id) {
        boolean deleted = commandeProduitService.deleteCommandeProduit(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<CommandeProduitDto>> getCommandeProduitsByCommandeId(@PathVariable Integer commandeId) {
        List<CommandeProduitDto> commandeProduits = commandeProduitService.getCommandeProduitsByCommandeId(commandeId);
        return ResponseEntity.ok(commandeProduits);
    }
}