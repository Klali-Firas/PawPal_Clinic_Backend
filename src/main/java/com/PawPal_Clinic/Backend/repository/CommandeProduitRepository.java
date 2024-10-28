package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.CommandeProduit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeProduitRepository extends JpaRepository<CommandeProduit, Integer> {
  }