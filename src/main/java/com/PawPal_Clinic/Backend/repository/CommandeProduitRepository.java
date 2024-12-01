package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.CommandeProduit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeProduitRepository extends JpaRepository<CommandeProduit, Integer> {
  List<CommandeProduit> findByCommandeId(Integer commandeId);

}