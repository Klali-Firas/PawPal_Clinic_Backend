package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class UtilisateurDto implements Serializable {
    private Integer id;
    private String email;
    private String role;
    private String prenom;
    private String nom;
    private String telephone;
    private Instant creeLe;
    private String refreshToken;

    public UtilisateurDto(Integer id, String email, String role, String prenom, String nom, String telephone, Instant creeLe, String refreshToken) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
        this.creeLe = creeLe;
        this.refreshToken = refreshToken;
    }

    public UtilisateurDto() {
    }

    // Getters and setters for all fields, including refreshToken
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtilisateurDto entity = (UtilisateurDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.role, entity.role) &&
                Objects.equals(this.prenom, entity.prenom) &&
                Objects.equals(this.nom, entity.nom) &&
                Objects.equals(this.telephone, entity.telephone) &&
                Objects.equals(this.creeLe, entity.creeLe) &&
                Objects.equals(this.refreshToken, entity.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, role, prenom, nom, telephone, creeLe, refreshToken);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "email = " + email + ", " +
                "role = " + role + ", " +
                "prenom = " + prenom + ", " +
                "nom = " + nom + ", " +
                "telephone = " + telephone + ", " +
                "creeLe = " + creeLe + ", " +
                "refreshToken = " + refreshToken + ")";
    }
}