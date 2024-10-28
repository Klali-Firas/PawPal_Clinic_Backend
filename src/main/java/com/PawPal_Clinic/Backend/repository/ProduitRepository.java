package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Integer> {
}