package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.CommandeDto;
import com.PawPal_Clinic.Backend.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @GetMapping
    public ResponseEntity<List<CommandeDto>> getAllCommandes() {
        List<CommandeDto> commandes = commandeService.getAllCommandes();
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Integer id) {
        Optional<CommandeDto> commande = commandeService.getCommandeById(id);
        return commande.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CommandeDto> createCommande(@RequestBody CommandeDto commandeDto) {
        CommandeDto createdCommande = commandeService.createCommande(commandeDto);
        return ResponseEntity.ok(createdCommande);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeDto> updateCommande(@PathVariable Integer id, @RequestBody CommandeDto commandeDto) {
        Optional<CommandeDto> updatedCommande = commandeService.updateCommande(id, commandeDto);
        return updatedCommande.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Integer id) {
        boolean deleted = commandeService.deleteCommande(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommandeDto>> getCommandesByUserId(@PathVariable Integer userId) {
        List<CommandeDto> commandes = commandeService.getCommandesByUserId(userId);
        return ResponseEntity.ok(commandes);
    }
}