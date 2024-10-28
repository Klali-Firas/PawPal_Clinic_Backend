package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.PawPal_Clinic.Backend.model.Animaux}
 */
public class AnimauxDto implements Serializable {
    private Integer id;
    private Integer proprietaireId;
    private String nom;
    private String race;
    private Integer age;
    private String historiqueMedical;
    private Instant creeLe;

    public AnimauxDto() {
    }

    public AnimauxDto(Integer id, Integer proprietaireId, String nom, String race, Integer age, String historiqueMedical, Instant creeLe) {
        this.id = id;
        this.proprietaireId = proprietaireId;
        this.nom = nom;
        this.race = race;
        this.age = age;
        this.historiqueMedical = historiqueMedical;
        this.creeLe = creeLe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Integer proprietaireId) {
        this.proprietaireId = proprietaireId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimauxDto entity = (AnimauxDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.proprietaireId, entity.proprietaireId) &&
                Objects.equals(this.nom, entity.nom) &&
                Objects.equals(this.race, entity.race) &&
                Objects.equals(this.age, entity.age) &&
                Objects.equals(this.historiqueMedical, entity.historiqueMedical) &&
                Objects.equals(this.creeLe, entity.creeLe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, proprietaireId, nom, race, age, historiqueMedical, creeLe);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "proprietaireId = " + proprietaireId + ", " +
                "nom = " + nom + ", " +
                "race = " + race + ", " +
                "age = " + age + ", " +
                "historiqueMedical = " + historiqueMedical + ", " +
                "creeLe = " + creeLe + ")";
    }
}