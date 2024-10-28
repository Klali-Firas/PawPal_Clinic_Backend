package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
}