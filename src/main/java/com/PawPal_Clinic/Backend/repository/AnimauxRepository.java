package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.Animaux;
import com.PawPal_Clinic.Backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnimauxRepository extends JpaRepository<Animaux, Integer> {

    List<Animaux> findByProprietaireId(Integer proprietaireId);

    Optional<Animaux> findAnimauxByProprietaire(Utilisateur proprietaire);
}