package com.PawPal_Clinic.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "services_animaux")
public class ServicesAnimaux {
    @Id
    @ColumnDefault("nextval('services_animaux_service_animal_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_animal_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animaux animal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinaire_id", nullable = false)
    private Utilisateur veterinaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "date_service", nullable = false)
    private Instant dateService;

    @Column(name = "remarques", length = Integer.MAX_VALUE)
    private String remarques;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Animaux getAnimal() {
        return animal;
    }

    public void setAnimal(Animaux animal) {
        this.animal = animal;
    }

    public Utilisateur getVeterinaire() {
        return veterinaire;
    }

    public void setVeterinaire(Utilisateur veterinaire) {
        this.veterinaire = veterinaire;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Instant getDateService() {
        return dateService;
    }

    public void setDateService(Instant dateService) {
        this.dateService = dateService;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

}