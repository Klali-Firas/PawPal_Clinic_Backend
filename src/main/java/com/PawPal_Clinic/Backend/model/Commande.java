package com.PawPal_Clinic.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "commandes")
public class Commande {
    @Id
    @ColumnDefault("nextval('commandes_commande_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commande_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private Utilisateur proprietaire;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_commande",insertable = false, updatable = false)
    private Instant dateCommande;

    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

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

    public Instant getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

}