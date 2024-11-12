package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {
  List<RendezVous> findByVeterinaireId(Integer veterinaireId);
  }