package com.PawPal_Clinic.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur implements Serializable {
    @Id
    @ColumnDefault("nextval('utilisateurs_utilisateur_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utilisateur_id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Column(name = "prenom", length = 50)
    private String prenom;

    @Column(name = "nom", length = 50)
    private String nom;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "cree_le", insertable = false, updatable = false)
    private Instant creeLe;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    // Getters and setters for all fields, including refreshToken
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(Instant creeLe) {
        this.creeLe = creeLe;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}