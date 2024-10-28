package com.PawPal_Clinic.Backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "animaux")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Animaux implements Serializable {
    @Id
    @ColumnDefault("nextval('animaux_animal_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private Utilisateur proprietaire;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "race", length = 50)
    private String race;

    @Column(name = "age")
    private Integer age;

    @Column(name = "historique_medical", length = Integer.MAX_VALUE)
    private String historiqueMedical;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "cree_le",updatable = false, insertable = false)
    private Instant creeLe;

    public Animaux() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHistoriqueMedical() {
        return historiqueMedical;
    }

    public void setHistoriqueMedical(String historiqueMedical) {
        this.historiqueMedical = historiqueMedical;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(Instant creeLe) {
        this.creeLe = creeLe;
    }

}