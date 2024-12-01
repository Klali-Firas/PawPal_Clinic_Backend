package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findByProprietaireId(Integer proprietaireId);

}