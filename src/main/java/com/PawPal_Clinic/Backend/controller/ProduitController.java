package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.ProduitDto;
import com.PawPal_Clinic.Backend.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProduitDto>> getAllProduits() {
        List<ProduitDto> produits = produitService.getAllProduits();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDto> getProduitById(@PathVariable Integer id) {
        Optional<ProduitDto> produit = produitService.getProduitById(id);
        return produit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProduitDto> createProduit(@RequestBody ProduitDto produitDto) {
        ProduitDto createdProduit = produitService.createProduit(produitDto);
        return ResponseEntity.ok(createdProduit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDto> updateProduit(@PathVariable Integer id, @RequestBody ProduitDto produitDto) {
        Optional<ProduitDto> updatedProduit = produitService.updateProduit(id, produitDto);
        return updatedProduit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Integer id) {
        boolean deleted = produitService.deleteProduit(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}