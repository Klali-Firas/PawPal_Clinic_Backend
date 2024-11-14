package com.PawPal_Clinic.Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "rendez_vous")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RendezVous {
    @Id
    @ColumnDefault("nextval('rendez_vous_rendez_vous_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rendez_vous_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animaux animal;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "veterinaire_id")
    private Utilisateur veterinaire;

    @Column(name = "date_rendez_vous", nullable = false)
    private Instant dateRendezVous;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "motif", nullable = false)
    private Service motif;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "cree_le", insertable = false, updatable = false)
    private Instant creeLe;

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

    public Instant getDateRendezVous() {
        return dateRendezVous;
    }

    public void setDateRendezVous(Instant dateRendezVous) {
        this.dateRendezVous = dateRendezVous;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Service getMotif() {
        return motif;
    }

    public void setMotif(Service motif) {
        this.motif = motif;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(Instant creeLe) {
        this.creeLe = creeLe;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}