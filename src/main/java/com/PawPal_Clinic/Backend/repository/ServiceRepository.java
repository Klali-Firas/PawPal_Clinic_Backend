package com.PawPal_Clinic.Backend.repository;

import com.PawPal_Clinic.Backend.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}