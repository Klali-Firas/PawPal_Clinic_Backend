package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.ServicesAnimaux;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesAnimauxRepository extends JpaRepository<ServicesAnimaux, Integer> {
  }