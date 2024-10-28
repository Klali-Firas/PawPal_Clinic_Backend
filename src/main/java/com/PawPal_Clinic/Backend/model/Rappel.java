package com.PawPal_Clinic.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "rappels")
public class Rappel {
    @Id
    @ColumnDefault("nextval('rappels_rappel_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rappel_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animaux animal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinaire_id", nullable = false)
    private Utilisateur veterinaire;

    @Column(name = "date_rappel", nullable = false)
    private Instant dateRappel;

    @Column(name = "type_rappel", length = 100)
    private String typeRappel;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "cree_le", insertable = false, updatable = false)
    private Instant creeLe;

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

    public Instant getDateRappel() {
        return dateRappel;
    }

    public void setDateRappel(Instant dateRappel) {
        this.dateRappel = dateRappel;
    }

    public String getTypeRappel() {
        return typeRappel;
    }

    public void setTypeRappel(String typeRappel) {
        this.typeRappel = typeRappel;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(Instant creeLe) {
        this.creeLe = creeLe;
    }

}