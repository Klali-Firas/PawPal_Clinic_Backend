package com.PawPal_Clinic.Backend.service;

import com.PawPal_Clinic.Backend.dto.ProduitDto;
import com.PawPal_Clinic.Backend.model.Produit;
import com.PawPal_Clinic.Backend.repository.ProduitRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ProduitDto> getAllProduits() {
        return produitRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProduitDto> getProduitById(Integer id) {
        return produitRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public ProduitDto createProduit(ProduitDto produitDto) {
        Produit produit = convertToEntity(produitDto);
        Produit savedProduit = produitRepository.save(produit);
        entityManager.refresh(savedProduit);
        return convertToDto(savedProduit);
    }

    @Transactional
    public Optional<ProduitDto> updateProduit(Integer id, ProduitDto produitDto) {
        if (!produitRepository.existsById(id)) {
            return Optional.empty();
        }
        Produit produit = convertToEntity(produitDto);
        produit.setId(id);
        Produit updatedProduit = produitRepository.save(produit);
        return Optional.of(convertToDto(updatedProduit));
    }

    @Transactional
    public boolean deleteProduit(Integer id) {
        if (produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ProduitDto convertToDto(Produit produit) {
        return new ProduitDto(produit.getId(), produit.getNomProduit(), produit.getDescription(), produit.getPrix(), produit.getQuantiteStock(), produit.getCreeLe());
    }

    private Produit convertToEntity(ProduitDto produitDto) {
        Produit produit = new Produit();
        produit.setNomProduit(produitDto.getNomProduit());
        produit.setDescription(produitDto.getDescription());
        produit.setPrix(produitDto.getPrix());
        produit.setQuantiteStock(produitDto.getQuantiteStock());
        produit.setCreeLe(produitDto.getCreeLe());
        return produit;
    }
}