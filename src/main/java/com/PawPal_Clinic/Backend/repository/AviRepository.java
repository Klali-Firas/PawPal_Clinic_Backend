package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.Avi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AviRepository extends JpaRepository<Avi, Integer> {
  List<Avi> findByRendezVousId(Integer rendezVousId);
  Optional<Avi> findByRendezVousIdAndProprietaireId(Integer rendezVousId, Integer proprietaireId);

}